package com.dh.sp.reactive.mongo.exception;

public class ItemNotFound extends RuntimeException {

  public ItemNotFound(final String msg){
    super(msg);
  }
}
