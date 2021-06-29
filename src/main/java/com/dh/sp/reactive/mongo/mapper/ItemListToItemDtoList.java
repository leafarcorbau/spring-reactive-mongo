package com.dh.sp.reactive.mongo.mapper;

import com.dh.sp.reactive.mongo.config.MapStructConfig;
import com.dh.sp.reactive.mongo.document.Item;
import com.dh.sp.reactive.mongo.dto.ItemDto;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class,
    uses = {ItemToItemDto.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ItemListToItemDtoList {

  List<ItemDto> map (final List<Item> itemList);
}
