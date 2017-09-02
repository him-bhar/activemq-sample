package com.himanshu.message.transfer;

public interface MessageReceiver<T> {
  void receive(T t);
}
