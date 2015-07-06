package cn.godzilla.rpc.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.benchmark.dataobject.FullAddress;
import cn.godzilla.rpc.benchmark.dataobject.Person;
import cn.godzilla.rpc.benchmark.dataobject.PersonInfo;
import cn.godzilla.rpc.benchmark.dataobject.PersonStatus;
import cn.godzilla.rpc.benchmark.dataobject.Phone;
import cn.godzilla.rpc.benchmark.service.HelloService;

/**
 * @author ding.lid
 */
public class ClientMain {
    private static volatile int size = 3;

    
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, InterruptedException {
    	final RpcFactory rpcFactory = Util.getRpcFactoryImpl();
    	String serverIp = "127.0.0.1";
    	
        final HelloService reference = rpcFactory.getReference(HelloService.class, serverIp);
        
    	//while(true){
    		System.out.println("weak");
    		Person person = genPerson();
    		//Person ret = reference.helloPerson(person);
    		String ret = reference.helloWorld("111");
    		System.out.println("invoke ok ret" + ret);
    	//}
    	// block main thread to prevent process exit.
    }
    
    public static Person genPerson() {
        Person person = new Person();
        person.setPersonId("id1");
        person.setLoginName("name1");
        person.setStatus(PersonStatus.ENABLED);

        int sz = Math.max(0, size - 1);
        byte[] attachment = new byte[1024 * sz + 512]; // data size K
        Random random = new Random();
        random.nextBytes(attachment);
        person.setAttachment(attachment);

        ArrayList<Phone> phones = new ArrayList<Phone>();
        Phone phone1 = new Phone("86", "0571", "11223344", "001");
        Phone phone2 = new Phone("86", "0571", "11223344", "002");
        phones.add(phone1);
        phones.add(phone2);

        PersonInfo info = new PersonInfo();
        info.setPhones(phones);
        Phone fax = new Phone("86", "0571", "11223344", null);
        info.setFax(fax);
        FullAddress addr = new FullAddress("CN", "zj", "1234", "Road1", "333444");
        info.setFullAddress(addr);
        info.setMobileNo("1122334455");
        info.setMale(true);
        info.setDepartment("mw");
        info.setHomepageUrl("www.taobao.com");
        info.setJobTitle("dev");
        info.setName("name2");

        person.setInfo(info);

        return person;
    }

    private static void scribblePerson(Person p) {
        p.setStatus(p.getStatus() == PersonStatus.ENABLED ? PersonStatus.DISABLED : PersonStatus.ENABLED);
        p.getAttachment()[0]++;
    }

    private static ThreadLocal<Person> persons = new ThreadLocal<Person>() {
        @Override
        protected Person initialValue() {
            return genPerson();
        }
    };

    /*public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("s", "server", true, "service server ip, default 127.0.0.1");
        options.addOption("d", "delay", true, "count delay before start count(ms), default 0ms");
        options.addOption("t", "time", true, "run duration(ms), default 3000ms");
        options.addOption("r", "threads", true, "run thread count, default use the value from method getClientThreads");
        options.addOption("z", "size", true, "data size, default 3K");
        options.addOption("o", "output", true, "benchmark result output file, default output result to console");
        options.addOption("h", "help", false, "help");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Usage", options, true);
            return;
        }

        final String serverIp;
        if(!cmd.hasOption("s")) {
            serverIp = "127.0.0.1";
        }
        else {
            serverIp = cmd.getOptionValue("s");
            System.out.println("Use server from cmd line: " + serverIp);
        }
        final long countDelay;
        if(!cmd.hasOption("d")) {
            countDelay = 0; // default no delay
        }
        else {
            countDelay = Integer.parseInt(cmd.getOptionValue("d"));
            System.out.println("Use run delay from cmd line: " + countDelay + "ms");
        }
        final long runDuration;
        if(!cmd.hasOption("t")) {
            runDuration = 3 * 1000; // default just run 3s
        }
        else {
            runDuration = Integer.parseInt(cmd.getOptionValue("t"));
            System.out.println("Use run time from cmd line: " + runDuration + "ms");
        }
        final int threadCount;
        if(!cmd.hasOption("r")) {
            threadCount = -1;
        }
        else {
            threadCount = Integer.parseInt(cmd.getOptionValue("r"));
            System.out.println("Use thread count from cmd line: " + threadCount);
        }
        if(cmd.hasOption("z")) {
            int dataSize = Integer.parseInt(cmd.getOptionValue("z"));
            if(dataSize > 0) size = dataSize;
            System.out.println("Use data size from cmd line: " + dataSize + "K");
        }
        final String outputFile;
        if(!cmd.hasOption("o")) {
            outputFile = null;
        }
        else {
            outputFile = cmd.getOptionValue("o");
            System.out.println("Use output file from cmd line: " + outputFile);
        }

        final RpcFactory rpcFactory = Util.getRpcFactoryImpl();
        final HelloService reference = rpcFactory.getReference(HelloService.class, serverIp);

        final AtomicLong counter = new AtomicLong();
        final AtomicBoolean stopped = new AtomicBoolean(false);
        final AtomicBoolean correct = new AtomicBoolean(true);
        final Thread mainThread = Thread.currentThread();

        final int runThreads = threadCount > 0 ? threadCount : rpcFactory.getClientThreads();
        ExecutorService executorService = Executors.newFixedThreadPool(runThreads);
        for(int i = 0; i < runThreads; ++i) {
            Runnable invoker = new Runnable() {
                @Override
                public void run() {
                    while(!stopped.get()) {
                        try {
                            Person person = persons.get();
                            scribblePerson(person);

                            Person ret = reference.helloPerson(person);
                            if(counter.get() % 100 == 0 && !person.equals(ret)) {
                                correct.set(false);
                                stopped.set(true);
                                mainThread.interrupt();
                            }

                            counter.incrementAndGet();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            };

            executorService.execute(invoker);
        }

        Thread.sleep(countDelay);

        final long beginCount = counter.get();
        final long startTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            long left = runDuration - (now - startTime);

            if(left <= 0 || stopped.get()) break;
            try {
                Thread.sleep(left);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        final long invokeTime = counter.get() - beginCount;
        stopped.set(true);

        // shutdown() will block. just ignore shutdown operation, since call System.exit() later
        // executorService.shutdown();

        printResult(rpcFactory.getAuthorId(), correct.get(), runDuration, invokeTime, outputFile);

        System.exit(0);
    }

    private static void printResult(String authorId, boolean correct, long runDuration, long invokeTime, String outputFile) throws IOException {
        String[] formats = {"%tY-%<tm-%<td %<tH:%<tM:%<tS", "%s", "%s", "%d", "%.2f", "%d", "%d"};
        String[] humanFormats ={"%s", "Author: %s", "Result: %s", "Size: %sK", "TPS: %s", "InvokeCount: %s", "RunTime: %sms"};

        StringBuilder wholeFormat = new StringBuilder();
        if(outputFile == null) { // output result to console for human
            int outputCount = formats.length;
            if(!correct) {
                outputCount = 3;
            }
            for(int i = 0; i < outputCount; ++i) {
                if(i != 0) {
                    wholeFormat.append(", ");
                }
                wholeFormat.append(String.format(humanFormats[i], formats[i]));
            }
            wholeFormat.append(System.getProperty("line.separator"));

            System.out.printf(wholeFormat.toString(),
                    System.currentTimeMillis(), authorId, correct, size,
                    1.0 * invokeTime / runDuration * 1000, invokeTime, runDuration);
            System.out.flush();
        }
        else { // output result to file, as csv file
            int outputCount = formats.length;
            if(!correct) {
                outputCount = 3;
            }
            for(int i = 0; i < outputCount; ++i) {
                if(i != 0) {
                    wholeFormat.append(", ");
                }
                wholeFormat.append(formats[i]);
            }
            wholeFormat.append(System.getProperty("line.separator"));

            PrintWriter out = new PrintWriter(new FileWriter(outputFile, true));
            out.printf(wholeFormat.toString(),
                    System.currentTimeMillis(), authorId, correct, size,
                    1.0 * invokeTime / runDuration * 1000, invokeTime, runDuration);
            out.flush();
        }
    }*/
    
    
}
