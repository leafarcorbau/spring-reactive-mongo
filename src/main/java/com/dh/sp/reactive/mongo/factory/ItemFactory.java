package com.dh.sp.reactive.mongo.factory;

import com.dh.sp.reactive.mongo.document.Item;
import java.util.UUID;

public class ItemFactory {

  public static Item.ItemBuilder create(final UUID seed){
    return Item.builder()
        .id(seed)
        .description("description-"+seed.toString())
        .price(1D);
  }
}
