package com.dh.sp.reactive.mongo.manager;

import com.dh.sp.reactive.mongo.dto.CreateItemDto;
import com.dh.sp.reactive.mongo.dto.ItemDto;
import com.dh.sp.reactive.mongo.dto.ItemsDto;
import com.dh.sp.reactive.mongo.dto.UpdateItemDto;
import com.dh.sp.reactive.mongo.mapper.CreateItemDtoToItem;
import com.dh.sp.reactive.mongo.mapper.ItemListToItemDtoList;
import com.dh.sp.reactive.mongo.mapper.ItemToItemDto;
import com.dh.sp.reactive.mongo.service.ItemService;
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
public class ItemManager {

  ItemService itemService;
  ItemToItemDto itemToItemDto;
  CreateItemDtoToItem createItemDtoToItem;
  ItemListToItemDtoList itemListToItemDtoList;

  public Mono<ItemsDto> findAll(){
    return itemService.findAll()
        .map(items -> ItemsDto
            .builder()
            .items(itemListToItemDtoList.map(items))
            .build());
  }

  public Mono<ItemDto> findById(final UUID id){
    return itemService
        .findById(id)
        .map(item -> itemToItemDto.map(item));
  }

  public Mono<ItemDto> create(final CreateItemDto createItemDto){
    return itemService
        .create(createItemDtoToItem.map(createItemDto))
        .map(item -> itemToItemDto.map(item));
  }

  public Mono<Void> deleteById(final UUID id){
    return itemService.deleteById(id);
  }

  public Mono<ItemDto> update(final UpdateItemDto updateItemDto){
    return itemService
        .update(updateItemDto)
        .map(itemUpdated -> itemToItemDto.map(itemUpdated));
  }

}
