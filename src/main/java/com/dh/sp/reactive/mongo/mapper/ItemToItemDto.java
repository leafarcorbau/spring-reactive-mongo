package com.dh.sp.reactive.mongo.mapper;

import com.dh.sp.reactive.mongo.config.MapStructConfig;
import com.dh.sp.reactive.mongo.document.Item;
import com.dh.sp.reactive.mongo.dto.ItemDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface ItemToItemDto {

   ItemDto map(final Item item);
}
