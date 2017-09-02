package com.himanshu.activemq.exception;

public class MessagingException extends RuntimeException {
  public MessagingException(String message, Throwable cause) {
    super(message, cause);
  }
}
