package com.himanshu.activemq.receiver;

import com.himanshu.activemq.MQMessage;
import com.himanshu.activemq.exception.MessagingException;
import com.himanshu.message.transfer.ActionableListener;
import com.himanshu.message.transfer.Message;
import com.himanshu.message.transfer.MessageReceiver;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

public class JMSMessageReceiver implements MessageReceiver<Message> {

  private final Session session;
  private final boolean isTopic;
  private final String destinationName;
  private final MessageConsumer messageConsumer;
  private final ActionableListener actionableListener;
  private static final Logger LOG = LoggerFactory.getLogger(JMSMessageReceiver.class);

  public JMSMessageReceiver(Session session, String destinationName, boolean isTopic, ActionableListener actionableListener) throws JMSException {
    this(session, destinationName, isTopic, false, actionableListener, null);
  }

  public JMSMessageReceiver(Session session, String destinationName, boolean isTopic, boolean isDurableSubscription, ActionableListener actionableListener, String clientId) throws JMSException {
    this.session = session;
    this.isTopic = isTopic;
    this.destinationName = destinationName;


    if (this.isTopic) {
      Topic mqDestination = new ActiveMQTopic(this.destinationName);
      if (isDurableSubscription) {
        messageConsumer = this.session.createDurableSubscriber(mqDestination, clientId);
      } else {
        messageConsumer = this.session.createConsumer(mqDestination);
      }
    } else {
      ActiveMQDestination mqDestination = new ActiveMQQueue(this.destinationName);
      messageConsumer = this.session.createConsumer(mqDestination);
    }

    messageConsumer.setMessageListener(this::receiveFromMQ);
    this.actionableListener = actionableListener;
  }

  private void receiveFromMQ(javax.jms.Message message) {
    try {
      ActiveMQTextMessage textMsg = (ActiveMQTextMessage)message;
      MQMessage mqMsg = new MQMessage(textMsg.getText());
      receive(mqMsg);
    } catch (JMSException e) {
      throw new MessagingException("Error processing received message", e);
    }
  }

  @Override
  public void receive(Message message) {
    LOG.info("Receiving: {}", message);
    if (actionableListener != null) {
      actionableListener.onMessage(message);
    }
  }

}
