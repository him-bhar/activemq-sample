package com.himanshu.message.transfer;

public interface Message<T> extends Stringable {
  T get();
}
