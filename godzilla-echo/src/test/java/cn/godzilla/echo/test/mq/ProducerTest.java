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
package cn.godzilla.echo.test.mq;

import java.io.IOException;

import cn.godzilla.common.config.Config;
import cn.godzilla.echo.serialize.Serializer;
import cn.godzilla.echo.vo.EchoMessage;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;


/**
 * Producer，发送消息
 * 
 */
public class ProducerTest {
	public static final int STOP_RUN = 2;
    public static void main(String[] args) throws MQClientException, InterruptedException, IOException {
        DefaultMQProducer producer = new DefaultMQProducer(Config.getMqProducerName());
        producer.setNamesrvAddr(Config.getMqNamesrvAddr());
        producer.start();
        
        for (int i = 0; i < STOP_RUN; i++) {
        	EchoMessage echoMessage = EchoMessage.getInstance("wanglin", "mvn", ""+i);
            try {
                Message msg = new Message(Config.getMqTopic(),// topic
                    "echo",// tag
                    Serializer.serialize(echoMessage)// body
                        );
                SendResult sendResult = producer.send(msg);
                System.out.println(sendResult);
            }
            catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        producer.shutdown();
    }
}
