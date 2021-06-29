package com.dh.sp.reactive.mongo.controller.v2;

import com.dh.sp.reactive.mongo.dto.ErrorDto;
import com.dh.sp.reactive.mongo.exception.ItemNotFound;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExceptionHandler {

  public Mono<ServerResponse> buildErrorResponse(final Throwable e) {
    if(e instanceof ItemNotFound){
      return ServerResponse
          .status(HttpStatus.NOT_FOUND)
          .bodyValue(ErrorDto.builder()
              .errorCode(1)
              .msg(e.getMessage())
              .build());
    }else{
      return ServerResponse
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .bodyValue(ErrorDto.builder()
              .errorCode(2)
              .msg(e.getMessage())
              .build());
    }
  }

}
