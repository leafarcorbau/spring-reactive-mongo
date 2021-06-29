package com.dh.sp.reactive.mongo.controller.v2;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.dh.sp.reactive.mongo.dto.CreateItemDto;
import com.dh.sp.reactive.mongo.dto.ItemDto;
import com.dh.sp.reactive.mongo.dto.ItemsDto;
import com.dh.sp.reactive.mongo.dto.UpdateItemDto;
import com.dh.sp.reactive.mongo.manager.ItemManager;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemHandler {

  ItemManager itemManager;
  ExceptionHandler exceptionHandler;

  public Mono<ServerResponse> findAll(final ServerRequest serverRequest) {
    return itemManager.findAll()
        .flatMap(itemsDto -> ServerResponse.ok()
        .bodyValue(itemsDto));
  }

  public Mono<ServerResponse> findById(final ServerRequest serverRequest) {
    final UUID id = UUID.fromString(serverRequest.pathVariable("id"));
    final Mono<ItemDto> result = itemManager.findById(id);
    return result.flatMap(itemDto -> ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(fromValue(itemDto)))
        .onErrorResume((e)-> exceptionHandler.buildErrorResponse(e));
  }

  public Mono<ServerResponse> create(final ServerRequest serverRequest) {
    final Mono<CreateItemDto> monoCreateItemDtoMono = serverRequest.bodyToMono(CreateItemDto.class);
    return monoCreateItemDtoMono
        .map(createItemDto -> itemManager.create(createItemDto))
        .flatMap(itemDtoMono ->  ServerResponse
            .status(HttpStatus.CREATED)
            .body(itemDtoMono, ItemsDto.class));
  }

  public Mono<ServerResponse> delete(final ServerRequest serverRequest) {
    final UUID id = UUID.fromString(serverRequest.pathVariable("id"));
    return  itemManager.deleteById(id)
        .flatMap( v -> ServerResponse.ok().build())
        .onErrorResume((e)-> exceptionHandler.buildErrorResponse(e));
  }

  public Mono<ServerResponse> update(final ServerRequest serverRequest) {
    final Mono<UpdateItemDto> monoUpdateItemDtoMono = serverRequest.bodyToMono(UpdateItemDto.class);
    return monoUpdateItemDtoMono
        .flatMap(updateItemDto ->  itemManager.update(updateItemDto))
        .flatMap(itemDtoMono -> ServerResponse.ok().bodyValue(itemDtoMono))
        .onErrorResume((e)-> exceptionHandler.buildErrorResponse(e));
  }
}
