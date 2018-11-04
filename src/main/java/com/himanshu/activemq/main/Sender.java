package com.himanshu.activemq.main;

import com.himanshu.activemq.MQMessage;
import com.himanshu.activemq.factory.SingleVMConnectionFactory;
import com.himanshu.activemq.receiver.JMSMessageReceiver;
import com.himanshu.activemq.sender.JMSMessageSender;
import com.himanshu.message.transfer.Message;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class Sender {
  public static void main(String[] args) throws JMSException, InterruptedException {
    Connection connection = SingleVMConnectionFactory.getInstance().getConnectionFactory().createConnection();
    Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE  );
    Thread senderThread = new Thread(() -> {
      try {
        JMSMessageSender sender = new JMSMessageSender(session, "test.destination", true);
        IntStream.rangeClosed(1, 5).asLongStream().forEach((l) -> sender.send(new MQMessage("This is a test message sent at: "+ LocalDateTime.now())));
      } catch (JMSException e) {
        throw new RuntimeException(e);
      }

    }, "sender-thread");
    senderThread.start();
    connection.start();
    Thread t = new Thread(() -> {
      try {
        Thread.sleep(5000l);
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
