/**
 * TODO
 * 
 */
package com.aitongyi.rabbitmq.helloworld;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 消息消费者
 * 
 * @author hushuang
 * 
 */
public class C {

	private final static String QUEUE_NAME = "search_queue";

	public static void main(String[] argv) throws Exception {
			
	        // 创建连接工厂  
	        ConnectionFactory factory = new ConnectionFactory();  
	        //	      设置RabbitMQ地址  
			factory.setHost("10.165.124.32");
			factory.setPort(5672);
			factory.setUsername("admin");
			factory.setPassword("f324fd2dwewe");
	        //	      创建一个新的连接  
	        Connection connection = factory.newConnection();  
	        //	      创建一个频道  
	        final Channel channel = connection.createChannel();  
	        //	      声明要关注的队列 -- 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。  
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
	        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");  
	        //	  DefaultConsumer类实现了Consumer接口，通过传入一个频道，告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery  
	        Consumer consumer = new DefaultConsumer(channel) {  
	            @Override  
	            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {  
	                String message = new String(body, "UTF-8");  
	                System.out.println("C [x] Received '" + message + "'"); 
	                try {
						System.out.println("");
					} catch (Exception e) {
						
					}finally {
	                    System.out.println("Worker1 [x] Done");
	                    // 消息处理完成确认
	                    channel.basicAck(envelope.getDeliveryTag(), false);
	                }
	            }  
	        };  
	        //自动回复队列应答 -- RabbitMQ中的消息确认机制
	        channel.basicConsume(QUEUE_NAME, false, consumer);  
	}
	
	

	@Test
	public void test2(){
		Double[] array={2.0,3.0};
		getMax(array);
	}
	
	public <T extends Number> T getMax(T[] array){
	    T max = null;
	    for(T element : array){
	        max = element;
	        System.out.println(max);
	    }
	    return max;
	}
}
