/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.godzilla.echo.rocketmq;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import cn.godzilla.common.config.Config;
import cn.godzilla.echo.serialize.Serializer;
import cn.godzilla.echo.vo.EchoMessage;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;


/**
 * Producer，发送消息
 * 
 */
public class Producer {
	public static final int STOP_RUN = 2;
	public static DefaultMQProducer producer;
	public volatile static boolean isStart = false;
	
	private static AtomicInteger id = new AtomicInteger();
	private int getUniqueId(){
		return id.addAndGet(1);
	}
	
	public static synchronized void start() throws IOException, MQClientException {
		if(producer==null) {
			producer = new DefaultMQProducer(Config.getMqProducerName()+id);
	        producer.setNamesrvAddr(Config.getMqNamesrvAddr());
	        producer.start();
	        isStart = true;
		}
	}
	
	public static boolean sendMessageToWeb(EchoMessage echoMessage) {
		try {
            Message msg = new Message(Config.getMqTopic(),// topic
                "echo",// tag
                Serializer.serialize(echoMessage)// body
                    );
            SendResult sendResult = producer.send(msg);
            
            return sendResult.getSendStatus().equals(SendStatus.SEND_OK);
        }  catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public boolean isStart() {
		return isStart;
	}
	
	public synchronized void shutdown() {
		producer.shutdown();
		isStart = false;
	}
}
