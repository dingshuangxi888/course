package com.dean.course6.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by dean on 15/1/11.
 */
public class Receiver {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;

        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");

        try {
            connection = connectionFactory.createConnection();

            connection.start();

            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("FirstQueue");

            consumer = session.createConsumer(destination);

            receiveMessage(consumer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void receiveMessage(MessageConsumer consumer) throws Exception {
        while (true) {
            TextMessage message = (TextMessage) consumer.receive(100000);
            if (message != null) {
                System.out.println("收到消息：" + message.getText());
            }
        }
    }
}
