package com.dh.sp.reactive.mongo.controller.v2;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemRouter {

  private static final String URL = "/v2";

  @Bean
  public RouterFunction<ServerResponse> route(final ItemHandler itemHandler) {
    return RouterFunctions
        .route(GET(URL + "/items")
                .and(accept(MediaType.APPLICATION_JSON)),
            itemHandler::findAll)
        .andRoute(GET(URL + "/items/{id}")
                .and(accept(MediaType.APPLICATION_JSON)),
            itemHandler::findById)
        .andRoute(POST(URL + "/items")
                .and(accept(MediaType.APPLICATION_JSON))
                .and(contentType(MediaType.APPLICATION_JSON)),
            itemHandler::create)
        .andRoute(DELETE(URL + "/items/{id}")
                .and(accept(MediaType.APPLICATION_JSON)),
            itemHandler::delete)
        .andRoute(PUT(URL + "/items")
                .and(accept(MediaType.APPLICATION_JSON))
                .and(contentType(MediaType.APPLICATION_JSON)),
            itemHandler::update);
  }
}
