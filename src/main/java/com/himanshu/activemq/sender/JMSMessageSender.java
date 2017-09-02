package com.himanshu.activemq.sender;

import com.himanshu.activemq.exception.MessagingException;
import com.himanshu.message.transfer.Message;
import com.himanshu.message.transfer.MessageSender;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class JMSMessageSender implements MessageSender<Message> {

  private final Session session;
  private final String destinationName;
  private final boolean isTopic;
  private final MessageProducer messageProducer;

  public JMSMessageSender(Session session, String destination, boolean isTopic) throws JMSException {
    this.session = session;
    this.destinationName = destination;
    ActiveMQDestination mqDestination = null;
    this.isTopic = isTopic;
    if (this.isTopic) {
      mqDestination = new ActiveMQTopic(this.destinationName);
    } else {
      mqDestination = new ActiveMQQueue(this.destinationName);
    }
    messageProducer = this.session.createProducer(mqDestination);
  }

  @Override
  public void send(Message message) {
    try {
      ActiveMQTextMessage textMsg = new ActiveMQTextMessage();
      textMsg.setText(message.getStringified());
      this.messageProducer.send(textMsg);
    } catch (JMSException e) {
      throw new MessagingException("Message sending failed: "+message, e);
    }
  }

}
