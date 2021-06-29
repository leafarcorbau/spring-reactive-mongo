package com.dh.sp.reactive.mongo.factory;

import com.dh.sp.reactive.mongo.dto.ItemDto;
import java.util.UUID;

public class ItemDtoFactory {

  public static ItemDto.ItemDtoBuilder create(final UUID seed){
    return ItemDto.builder()
        .id(seed)
        .description("description-"+seed)
        .price(1D);
  }
}
