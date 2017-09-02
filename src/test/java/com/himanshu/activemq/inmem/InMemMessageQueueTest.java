package com.himanshu.activemq.inmem;

import com.himanshu.activemq.MQMessage;
import com.himanshu.activemq.factory.SingleVMConnectionFactory;
import com.himanshu.activemq.receiver.JMSMessageReceiver;
import com.himanshu.activemq.sender.JMSMessageSender;
import com.himanshu.message.transfer.Message;
import com.himanshu.message.transfer.MessageReceiver;
import com.himanshu.message.transfer.MessageSender;
import org.junit.*;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class InMemMessageQueueTest {
  private Connection connection;
  private Session session;
  private MessageSender sender;
  private MessageReceiver receiver;
  private StringBuilder rcvdMessage;
  private String testMessage = "Hello World!";

  @BeforeClass
  public static void setup() {
    SingleVMConnectionFactory.getInstance(); //Initializing VM
  }

  @Before
  public void testSetup() throws JMSException {
    rcvdMessage = new StringBuilder();
    connection = SingleVMConnectionFactory.getInstance().getConnectionFactory().createConnection();
    session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
    sender = new JMSMessageSender(session, "test.destination", true);
    receiver = new JMSMessageReceiver(session, "test.destination", true, (Message message) -> rcvdMessage.append(message.getStringified()));
    connection.start();
  }

  @Test
  public void shouldBeAbleToSendAndReceive() throws InterruptedException {
    sender.send(new MQMessage(testMessage));
    Thread.sleep(5000l);
    Assert.assertEquals(testMessage, rcvdMessage.toString());
  }

  @After
  public void testCleanup() throws JMSException {
    connection.stop();
  }
}
