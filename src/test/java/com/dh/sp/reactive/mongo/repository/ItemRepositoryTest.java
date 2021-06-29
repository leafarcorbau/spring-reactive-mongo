package com.dh.sp.reactive.mongo.repository;

import com.dh.sp.reactive.mongo.IntegrationTest;
import com.dh.sp.reactive.mongo.document.Item;
import com.dh.sp.reactive.mongo.factory.ItemFactory;
import com.dh.sp.reactive.mongo.repository.ItemRepository;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ItemRepositoryTest extends IntegrationTest {
  @Autowired
  private ItemRepository itemRepository;

  @BeforeEach@AfterEach
  public void clean(){
    itemRepository.deleteAll();
  }

  @Test
  public void getAll(){
    //Given
    final Item expected = itemRepository.save(ItemFactory.create(UUID.randomUUID()).build())
        .doOnNext(item -> System.out.println("Item:"+item))
        .block();
    //When
    final Flux<Item> actual = itemRepository.findAll();
    //Then
    StepVerifier.create(actual)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  public void getById(){
    //Given
    final Item expected = itemRepository.save(ItemFactory.create(UUID.randomUUID()).build())
        .doOnNext(item -> System.out.println("Item:"+item))
        .block();
    //When
    final Mono<Item> actual = itemRepository.findById(expected.getId());
    //Then
    StepVerifier.create(actual)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  public void getByPrice(){
    //Given
    final Item expected = itemRepository.save(ItemFactory.create(UUID.randomUUID()).price(2D).build())
        .doOnNext(item -> System.out.println("Item:"+item))
        .block();
    //When
    final Flux<Item> actual = itemRepository.findByPrice(expected.getPrice());
    //Then
    StepVerifier.create(actual)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  public void update(){
    //Given
    final Item expected = itemRepository.save(ItemFactory.create(UUID.randomUUID()).build())
        .doOnNext(item -> System.out.println("Item:"+item))
        .block();
    expected.setDescription("newDescription");

    //When
    final Mono<Item> actual = itemRepository.findById(expected.getId())
        .map(item -> {
          item.setDescription("newDescription");
          return item;
        }).flatMap(item -> itemRepository.save(item));
    //Then
    StepVerifier.create(actual)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  public void deleteById(){
    //Given
    final Item expected = itemRepository.save(ItemFactory.create(UUID.randomUUID()).build())
        .doOnNext(item -> System.out.println("Item:"+item))
        .block();

    //When
    itemRepository.deleteById(expected.getId()).block();
    //Then
    final Mono<Item> actual = itemRepository.findById(expected.getId());
    StepVerifier.create(actual)
        .expectSubscription()
        .verifyComplete();
  }

  @Test
  public void delete(){
    //Given
    final Item expected = itemRepository.save(ItemFactory.create(UUID.randomUUID()).build())
        .doOnNext(item -> System.out.println("Item:"+item))
        .block();

    //When
    itemRepository.delete(expected).block();
    //Then
    final Mono<Item> actual = itemRepository.findById(expected.getId());
    StepVerifier.create(actual)
        .expectSubscription()
        .verifyComplete();
  }
}
