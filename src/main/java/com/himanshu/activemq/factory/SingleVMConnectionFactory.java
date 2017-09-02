package com.himanshu.activemq.factory;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SingleVMConnectionFactory {
  private static SingleVMConnectionFactory ourInstance = new SingleVMConnectionFactory();

  public static SingleVMConnectionFactory getInstance() {
    return ourInstance;
  }

  private javax.jms.ConnectionFactory connectionFactory;

  private SingleVMConnectionFactory() {
    connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
  }

  public javax.jms.ConnectionFactory getConnectionFactory() {
    return connectionFactory;
  }
}
