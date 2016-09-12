/**
 * TODO
 * 
 */
package com.aitongyi.rabbitmq.publish;

/**
 * @author hushuang
 *
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


		/*direct （直连）
		topic （主题）
		headers （标题）
		fanout （分发）也有翻译为扇出的。*/
        //交换器的规则有：我们将使用【fanout】类型创建一个名称为 logs的交换器，
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

//		分发消息
		for(int i = 0 ; i < 5; i++){
			String message = "Hello World! " + i;
			/*第一个参数就是交换器的名称。如果输入“”空字符串，表示使用默认的匿名交换器。 
				第二个参数是【routingKey】路由线索 	*/
			//我们提供一个空字符串的routingkey，它的功能被交换器的分发类型代替了。
			 channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		     System.out.println(" [x] Sent '" + message + "'");
		}
        channel.close();
        connection.close();
    }
}
