package com.dh.sp.reactive.mongo.repository;

import com.dh.sp.reactive.mongo.document.Item;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, UUID> {

  Flux<Item> findByPrice(final Double price);
}
