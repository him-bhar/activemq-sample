package com.himanshu.activemq.main;

import com.himanshu.activemq.factory.SingleVMConnectionFactory;
import com.himanshu.activemq.receiver.JMSMessageReceiver;
import com.himanshu.message.transfer.Message;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

public class Receiver {
  public static void main(String[] args) throws JMSException, InterruptedException {
    Connection connection = SingleVMConnectionFactory.getInstance().getConnectionFactory().createConnection();
    StringBuilder rcvdMessage = new StringBuilder();
    Thread receiverThread = new Thread(() -> {
      try {

        boolean durableSubscription = true;
        if (durableSubscription) {
          String clientId = "client-id-1";
          connection.setClientID("conn"+clientId);
          Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          JMSMessageReceiver receiver = new JMSMessageReceiver(session, "test.destination", true, durableSubscription, (Message message) -> rcvdMessage.append(message.getStringified()), "rcvr"+clientId);
        } else {
          Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          JMSMessageReceiver receiver = new JMSMessageReceiver(session, "test.destination", true, (Message message) -> rcvdMessage.append(message.getStringified()));
        }
      } catch (JMSException e) {
        throw new RuntimeException(e);
      }

    }, "receiver-thread");
    receiverThread.start();
    receiverThread.join();
    String clientId = "client-id-1";

    connection.start();
    Thread t = new Thread(() -> {
      try {
        Thread.sleep(100000l);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
    t.start();
    t.join();
    connection.stop();
    connection.close();
  }
}
