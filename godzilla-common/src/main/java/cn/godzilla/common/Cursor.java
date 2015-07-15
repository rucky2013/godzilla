/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.godzilla.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To support multiple tail readers, we have a Cursor for each file name
 * 
 * It takes a File and optionally a starting offset in the file. From there it
 * attempts to open the file as a RandomAccessFile, and reads data using the
 * RAF's FileChannel into a ByteBuffer. As \n's are found in the buffer, new
 * event bodies are created and new Events are create and put into the sync
 * queue.
 * 
 * If a file rotate is detected, the previous RAF is closed, and the File with
 * the specified name is opened.
 */
public class Cursor {
  private static final Logger LOG = LoggerFactory.getLogger(Cursor.class);

  final BlockingQueue<String> sync;
  // For following a file name
  final File file;
  // For buffering reads
  final ByteBuffer buf = ByteBuffer.allocateDirect(Short.MAX_VALUE);
  // For closing file handles and getting FileChannels
  RandomAccessFile raf = null;
  // For reading data
  FileChannel in = null;
  
  byte[] left ;

  long lastFileMod;
  long lastChannelPos;
  long lastChannelSize;
  int readFailures;
  
  public long getLastChannelPos() {
	return lastChannelPos;
 }
  public long getLastChannelSize() {
	return lastChannelSize;
 }
  public long getLastFileMod() {
	return lastFileMod;
 }
  
	public byte[] getLeft() {
		return left;
	}
  
  public Cursor(BlockingQueue<String> sync, File f) {
    this(sync, f, 0, 0, 0,null);
  }

  public Cursor(BlockingQueue<String> sync, File f, long lastReadOffset,
      long lastFileLen, long lastMod,byte[]bts) {
    this.sync = sync;
    this.file = f;
    this.lastChannelPos = lastReadOffset;
    this.lastChannelSize = lastFileLen;
    this.lastFileMod = lastMod;
    this.readFailures = 0;
    if(bts != null){
    	this.buf.put(bts);
    }
  }

  /**
   * Setup the initial cursor position.
   */
  public void initCursorPos() throws InterruptedException {
    try {
      LOG.debug("initCursorPos " + file);
      raf = new RandomAccessFile(file, "r");
      raf.seek(lastChannelPos);
      LOG.debug("lastChannelPos:" + lastChannelPos);
      in = raf.getChannel();
    } catch (FileNotFoundException e) {
      resetRAF();
    } catch (IOException e) {
      resetRAF();
    }
  }

  /**
   * Flush any buffering the cursor has done. If the buffer does not end with
   * '\n', the remainder will get turned into a new event.
   * 
   * This assumes that any remainders in buf are a single event -- this
   * corresponds to the last lines of files that do not end with '\n'
   */
  void flush() throws InterruptedException {
    if (raf != null) {
      try {
        raf.close(); // release handles
      } catch (IOException e) {
        LOG.error("problem closing file " + e.getMessage(), e);
      }
    }
    in = null;
  }

  /**
   * Closes cursor and releases all resources used by it. NOTE: to flush any
   * instead.
   */
  public void close() {
    if (raf != null) {
      try {
        raf.close(); // release handles
      } catch (IOException e) {
        LOG.error("problem closing file " + e.getMessage(), e);
      }
    }

    in = null;
    buf.flip();
    ByteArrayOutputStream bot = new ByteArrayOutputStream();
    while (buf.hasRemaining()) {
        byte b = buf.get();
        bot.write(b);
    }
    left = bot.toByteArray();
    buf.clear();
  }

  /**
   * Restart random accessfile cursor
   * 
   * This assumes that the buf is in write mode, and that any remainders in buf
   * are last bytes of files that do not end with \n
   * 
   * @throws InterruptedException
   */
  void resetRAF() throws InterruptedException {
    LOG.debug("reseting cursor");
    flush();
    lastChannelPos = 0;
    lastFileMod = 0;
    readFailures = 0;
  }

  /**
   * There is a race here on file system states -- a truly evil dir structure
   * can change things between these calls. Nothing we can really do though.
   * 
   * Ideally we would get and compare inode numbers, but in java land, we can't
   * do that.
   * 
   * returns true if changed, false if not.
   */
  boolean checkForUpdates() throws IOException {
    LOG.debug("tail " + file + " : recheck");
    if (file.isDirectory()) { // exists but not a file
      IOException ioe = new IOException("Tail expects a file '" + file
          + "', but it is a dir!");
      LOG.error(ioe.getMessage());
      throw ioe;
    }

    if (!file.exists()) {
      LOG.debug("Tail '" + file + "': nothing to do, waiting for a file");
      return false; // do nothing
    }

    if (!file.canRead()) {
      throw new IOException("Permission denied on " + file);
    }

    // oh! f exists and is a file
    try {
      // let's open the file
      raf = new RandomAccessFile(file, "r");
      lastFileMod = file.lastModified();
      in = raf.getChannel();
      lastChannelPos = in.position();
      lastChannelSize = in.size();

      LOG.debug("Tail '" + file + "': opened last mod=" + lastFileMod
          + " lastChannelPos=" + lastChannelPos + " lastChannelSize="
          + lastChannelSize);
      return true;
    } catch (FileNotFoundException fnfe) {
      // possible because of file system race, we can recover from this.
      LOG.debug("Tail '" + file
          + "': a file existed then disappeared, odd but continue");
      return false;
    }
  }

  ByteArrayOutputStream bot = new ByteArrayOutputStream();
  
  public boolean extractLines(ByteBuffer buf) throws IOException, InterruptedException {
    boolean madeProgress = false;
    while (buf.hasRemaining()) {
      byte b = buf.get();
      // TODO windows: ('\r\n') line separators
      if (b == '\n') {
        String newline = new String(bot.toByteArray());
        sync.put(newline);
        bot.reset();
        madeProgress = true;
      }else{
    	  bot.write(b); 
      }
    }

    // rewind for any left overs
    buf.compact(); // shift leftovers to front.
    return madeProgress;
  }

  /**
   * Attempt to get new data.
   * 
   * Returns true if cursor's state has changed (progress was made)
   */
  public boolean tailBody() throws InterruptedException {
    try {
      // no file named f currently, needs to be opened.
      if (in == null) {
        LOG.debug("tail " + file + " : cur file is null");
        return checkForUpdates();
      }

      long chlen = in.size();
      boolean madeProgress = readAllFromChannel();

      if (madeProgress) {
        lastChannelSize = lastChannelPos; // this is more safe than in.size()
        // due to possible race conditions
        // NOTE: this is racy (but very very small chance): if file was
        // rotated right before execution of next line with the file of the
        // same length and this new file is never modified the logic in this
        // method will never discover the rotation.
        lastFileMod = file.lastModified();
        LOG.debug("tail " + file + " : new data found");
        return true;
      }

      // this may seem racy but race conds handled properly with
      // extra checks below
      long fmod = file.lastModified();
      long flen = file.length(); // length of filename

      // If nothing can be read from channel, then cases:
      // 1) no change -> return
      if (flen == lastChannelSize && fmod == lastFileMod) {
        LOG.debug("tail " + file + " : no change");
        return false;
      }

      // 2) file rotated
      LOG.debug("tail " + file + " : file rotated?");
      // a) rotated with file of same length
      if (flen == lastChannelSize && fmod != lastFileMod) {
        // this is not trivial situation: it can
        // be "false positive", so we want to be sure rotation with same
        // file length really happened by additional checks.
        // Situation is ultimately rare so doing time consuming/heavy
        // things is OK here
        LOG.debug("tail " + file + " : file rotated with new one with "
            + "same length?");
        raf.getFD().sync(); // Alex: not sure this helps at all...
        Thread.sleep(1000); // sanity interval: more data may be written
      }

      // b) "false positive" for file rotation due to race condition
      // during fetching file stats: actually more data was added into the
      // file (and hence it is visible in channel)
      if (in.size() != chlen) {
        LOG.debug("tail " + file + " : there's extra data to be read from "
            + "file, aborting file rotation handling");
        return true;
      }

      // c) again "false positive" for file rotation: file was truncated
      if (chlen < lastChannelSize) {
        LOG.debug("tail " + file + " : file was truncated, "
            + "aborting file rotation handling");
        lastChannelSize = chlen;
        lastChannelPos = chlen;
        lastFileMod = file.lastModified();
        in.position(chlen); // setting cursor to the last position of
        // truncated file
        return false;
      }
   // d) possable size channged without seen modification timestamp changed
      if (in.size() == chlen && file.length() == chlen
    		             && file.lastModified() != lastFileMod) {
    		          LOG.debug("tail " + file + " : only seen modified timestamp changed, "+ "postponing judgement");
    		         return false;
    		        }

      LOG.debug("tail " + file + " : file rotated!");
      resetRAF(); // resetting raf to catch up new file

      // if file is not empty report true to start reading from it without a
      // delay
      return flen > 0;

    } catch (IOException e) {
      LOG.debug(e.getMessage(), e);
      in = null;
      readFailures++;

      /*
       * Back off on retries after 3 failures so we don't burn cycles. Note that
       * this can exacerbate the race condition illustrated above where a file
       * is truncated, created, written to, and truncated / removed while we're
       * sleeping.
       */
      if (readFailures > 3) {
        LOG.warn("Encountered " + readFailures + " failures on "
            + file.getAbsolutePath() + " - sleeping");
        return false;
      }
    }
    return true;
  }

  private boolean readAllFromChannel() throws IOException, InterruptedException {
    // I make this a rendezvous because this source is being pulled
    // copy data from current file pointer to EOF to dest.
    boolean madeProgress = false;

    int rd;
    while ((rd = in.read(buf)) > 0) {
      madeProgress = true;

      // need char encoder to find line breaks in buf.
      lastChannelPos += (rd < 0 ? 0 : rd); // rd == -1 if at end of
      // stream.

      int lastRd = 0;
      boolean progress = false;
      do {

        if (lastRd == -1 && rd == -1) {
          return true;
        }

        buf.flip();

        // extract lines
        extractLines(buf);

        lastRd = rd;
      } while (progress); // / potential race

      // if the amount read catches up to the size of the file, we can fall
      // out and let another fileChannel be read. If the last buffer isn't
      // read, then it remain in the byte buffer.

    }

    LOG.debug("tail " + file + ": last read position " + lastChannelPos
        + ", madeProgress: " + madeProgress);

    return madeProgress;
  }
}