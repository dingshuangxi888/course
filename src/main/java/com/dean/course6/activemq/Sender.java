package com.dean.course6.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by dean on 15/1/11.
 */
public class Sender {
    private static final int SEND_NUMBER = 5;
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;

        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer producer;

        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");

        try {
            connection = connectionFactory.createConnection();

            connection.start();

            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("FirstQueue");

            producer = session.createProducer(destination);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            sendMessage(session, producer);

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(Session session, MessageProducer producer) throws Exception {
        for (int i = 0; i < SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMq 发送的消息" + i);
            System.out.println("发送消息：ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }

}
