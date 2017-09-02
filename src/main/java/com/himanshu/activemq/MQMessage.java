package com.himanshu.activemq;

import com.himanshu.message.transfer.Message;

import java.io.Serializable;

public class MQMessage implements Message<String>, Serializable {

  private final String message;

  public MQMessage(String message) {
    this.message = message;
  }

  @Override
  public String get() {
    return message;
  }

  @Override
  public String getStringified() {
    return message;
  }

  @Override
  public String toString() {
    return "MQMessage{" +
            "message='" + getStringified() + '\'' +
            '}';
  }
}
