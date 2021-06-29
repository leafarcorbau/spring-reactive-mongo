package com.dh.sp.reactive.mongo.service;

import static java.lang.String.format;

import com.dh.sp.reactive.mongo.document.Item;
import com.dh.sp.reactive.mongo.dto.UpdateItemDto;
import com.dh.sp.reactive.mongo.exception.ItemNotFound;
import com.dh.sp.reactive.mongo.repository.ItemRepository;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemService {

  private static final String ITEM_NOT_FOUND = "Item with Id: %s, was not found";

  ItemRepository itemRepository;

  public Mono<List<Item>> findAll(){
    return itemRepository.findAll()
        .collectList();
  }

  public Mono<Item> findById(final UUID id){
    return itemRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new ItemNotFound(format(ITEM_NOT_FOUND, id))));
  }

  public Mono<Item> create(final Item item){
    return itemRepository.save(item);
  }

  public Mono<Void> deleteById(final UUID id){
    return findById(id)
        .flatMap(item -> itemRepository.delete(item));
  }

  public Mono<Item> update(final UpdateItemDto updateItemDto){
    final UUID id = updateItemDto.getId();
    return findById(id)
        .flatMap(item -> {
          item.setDescription(updateItemDto.getDescription());
          item.setPrice(updateItemDto.getPrice());
          return itemRepository.save(item);});
  }
}
