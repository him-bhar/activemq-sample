package com.himanshu.message.transfer;

public interface MessageSender<T> {
  void send(T t);
}
