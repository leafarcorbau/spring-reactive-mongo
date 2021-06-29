package com.dh.sp.reactive.mongo.factory;

import com.dh.sp.reactive.mongo.dto.CreateItemDto;
import java.util.UUID;

public class CreateItemDtoFactory {

  public static CreateItemDto.CreateItemDtoBuilder create(final UUID seed){
    return CreateItemDto.builder()
        .description("description-"+seed.toString())
        .price(1D);
  }
}
