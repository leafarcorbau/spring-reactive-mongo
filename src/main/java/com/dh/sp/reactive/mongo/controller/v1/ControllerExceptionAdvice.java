package com.dh.sp.reactive.mongo.controller.v1;

import com.dh.sp.reactive.mongo.dto.ErrorDto;
import com.dh.sp.reactive.mongo.exception.ItemNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

  @ExceptionHandler(ItemNotFound.class)
  public ResponseEntity<ErrorDto> itemNotFoundHandler(final ItemNotFound ex){
    log.error(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorDto.builder()
        .errorCode(1)
        .msg(ex.getMessage())
        .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> exceptionHandler(final Exception ex){
    log.error(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorDto.builder()
        .errorCode(2)
        .msg(ex.getMessage())
        .build());
  }
}
