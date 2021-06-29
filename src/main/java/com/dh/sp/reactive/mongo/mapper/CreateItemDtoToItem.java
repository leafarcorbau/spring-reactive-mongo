package com.dh.sp.reactive.mongo.mapper;

import com.dh.sp.reactive.mongo.config.MapStructConfig;
import com.dh.sp.reactive.mongo.document.Item;
import com.dh.sp.reactive.mongo.dto.CreateItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapStructConfig.class)
public interface CreateItemDtoToItem {

  @Mappings({
      @Mapping(target = "id",  expression = "java(UUID.randomUUID())")
  })
  Item map(final CreateItemDto createItemDto);
}
