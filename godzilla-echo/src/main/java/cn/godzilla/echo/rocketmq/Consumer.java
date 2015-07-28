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
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.IOException;
import java.util.List;

import cn.godzilla.common.Constant;
import cn.godzilla.common.config.Config;
import cn.godzilla.echo.main.MainClass;
import cn.godzilla.echo.serialize.Serializer;
import cn.godzilla.echo.vo.EchoMessage;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Consumer，订阅消息
 */
public class Consumer implements Constant{

	public static void main(String[] args) throws InterruptedException, MQClientException, IOException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Config.getMqConsumerName()+"1");
		consumer.setNamesrvAddr(Config.getMqNamesrvAddr());
		/**
		 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
		 * 如果非第一次启动，那么按照上次消费的位置继续消费
		 */
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

		consumer.subscribe(Config.getMqTopic(), "*");

		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
				for (Message msg : msgs) {
					byte[] data = msg.getBody();
					EchoMessage echoMessage = null;
					try {
						echoMessage = Serializer.deserializer(data, EchoMessage.class);
					} catch(Exception e) {
						e.printStackTrace();
						continue;
					}
					String usernameArea = echoMessage.getUsername() + "-" + echoMessage.getArea();
					String info = echoMessage.getInfo();
					Channel channel = MainClass.channelsMap.get(usernameArea);
					try {
						if (channel!=null){//&&channel.isWritable()) {
							channel.write(new TextWebSocketFrame(info));
							channel.flush();
							System.out.println("message-send");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumer.start();

		System.out.println("Consumer Started.");
	}
}
