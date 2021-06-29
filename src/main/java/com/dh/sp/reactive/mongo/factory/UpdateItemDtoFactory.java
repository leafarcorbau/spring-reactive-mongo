package com.dh.sp.reactive.mongo.factory;

import com.dh.sp.reactive.mongo.dto.UpdateItemDto;
import java.util.UUID;

public class UpdateItemDtoFactory {

  public static UpdateItemDto.UpdateItemDtoBuilder create(final UUID seed){
    return UpdateItemDto.builder()
        .id(seed)
        .description("description-"+seed.toString())
        .price(1D);
  }
}
