package com.dh.sp.reactive.mongo.controller.v1;

import com.dh.sp.reactive.mongo.dto.CreateItemDto;
import com.dh.sp.reactive.mongo.dto.ItemDto;
import com.dh.sp.reactive.mongo.dto.ItemsDto;
import com.dh.sp.reactive.mongo.dto.UpdateItemDto;
import com.dh.sp.reactive.mongo.manager.ItemManager;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemController {

  @Autowired
  private ItemManager itemManager;

  @GetMapping("/items")
  public Mono<ResponseEntity<ItemsDto>> findAll(){
    return itemManager.findAll()
        .map(items -> ResponseEntity.ok(items));
  }

  @GetMapping("/items/{id}")
  public Mono<ResponseEntity<ItemDto>> findById(@PathVariable final UUID id){
    return itemManager.findById(id)
        .map(item -> ResponseEntity.ok(item));
  }

  @PostMapping(value = "/items")
  public Mono<ResponseEntity<ItemDto>> create(@RequestBody final CreateItemDto createItemDto){
    return itemManager.create(createItemDto)
        .map(itemSaved -> ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemSaved));
  }

  @DeleteMapping("/items/{id}")
  public Mono<ResponseEntity<Object>> deleteById(@PathVariable final UUID id){
    return itemManager.deleteById(id)
        .thenReturn(ResponseEntity.ok().build());
  }

  @PutMapping(value = "/items")
  public Mono<ResponseEntity<ItemDto>> update(@RequestBody final UpdateItemDto updateItemDto){
    return itemManager.update(updateItemDto)
        .map(itemDto -> ResponseEntity.ok(itemDto));
  }
}
