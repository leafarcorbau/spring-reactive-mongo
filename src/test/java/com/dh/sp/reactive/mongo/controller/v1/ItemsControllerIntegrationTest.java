package com.dh.sp.reactive.mongo.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;

import com.dh.sp.reactive.mongo.IntegrationTest;
import com.dh.sp.reactive.mongo.document.Item;
import com.dh.sp.reactive.mongo.dto.CreateItemDto;
import com.dh.sp.reactive.mongo.dto.ErrorDto;
import com.dh.sp.reactive.mongo.dto.ItemDto;
import com.dh.sp.reactive.mongo.dto.ItemsDto;
import com.dh.sp.reactive.mongo.dto.UpdateItemDto;
import com.dh.sp.reactive.mongo.factory.CreateItemDtoFactory;
import com.dh.sp.reactive.mongo.factory.ItemDtoFactory;
import com.dh.sp.reactive.mongo.factory.ItemFactory;
import com.dh.sp.reactive.mongo.factory.UpdateItemDtoFactory;
import com.dh.sp.reactive.mongo.mapper.ItemToItemDto;
import com.dh.sp.reactive.mongo.repository.ItemRepository;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemsControllerIntegrationTest extends IntegrationTest {

  public static final String URL = "/v1";
  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private ItemToItemDto itemToItemDto;

  @BeforeEach
  public void init(){
    itemRepository.deleteAll().block();
  }

  @Test
  public void findAll(){
    //Given
    final UUID seed = UUID.randomUUID();
    final ItemsDto expected = itemRepository.save(ItemFactory.create(seed).build())
    .map(item -> ItemsDto.builder().items(List.of(itemToItemDto.map(item))).build()).block();
    //When
    final EntityExchangeResult<ItemsDto> actual = webTestClient.get()
        .uri(URL +"/items")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isOk()
        .expectBody(ItemsDto.class)
        .returnResult();
    //Then
    assertThat(actual.getResponseBody())
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  public void findAll_withNoItems(){
    //Given
    final ItemsDto expected = ItemsDto.builder().items(Collections.EMPTY_LIST).build();
    //When
    final EntityExchangeResult<ItemsDto> actual = webTestClient.get()
        .uri(URL +"/items")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isOk()
        .expectBody(ItemsDto.class)
        .returnResult();
    //Then
    assertThat(actual.getResponseBody())
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  public void findById(){
    //Given
    final UUID seed = UUID.randomUUID();
    itemRepository.save(ItemFactory.create(seed).build()).block();
    final ItemDto expected = ItemDtoFactory.create(seed).build();
    //When
    final EntityExchangeResult<ItemDto> actual = webTestClient.get()
        .uri(URL +"/items/"+seed.toString())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isOk()
        .expectBody(ItemDto.class)
        .returnResult();

    //Then
    assertThat(actual.getResponseBody())
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  public void failFindById_withNotFound(){
    //Given
    final UUID seed = UUID.randomUUID();
    final ErrorDto expected = ErrorDto.builder()
        .errorCode(1)
        .msg("Item with Id: "+seed+", was not found")
        .build();
    //When
    final EntityExchangeResult<ErrorDto> actual =  webTestClient.get()
        .uri(URL +"/items/"+seed.toString())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody(ErrorDto.class)
        .returnResult();

    //Then
    assertThat(actual.getResponseBody())
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  public void create(){
    //Given
    final UUID seed = UUID.randomUUID();
    final CreateItemDto createItemDto = CreateItemDtoFactory.create(seed).build();
    final Item expected = ItemFactory.create(seed).build();
    //When
    final EntityExchangeResult<Item> actual = webTestClient.post()
        .uri(URL +"/items")
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(createItemDto)
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isCreated()
        .expectBody(Item.class)
        .returnResult();
    //Then
    assertThat(actual.getResponseBody())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expected);
  }

  @Test
  public void deleteById(){
    //Given
    final UUID seed = UUID.randomUUID();
    itemRepository.save(ItemFactory.create(seed).build()).block();
    //When
     webTestClient.delete()
        .uri(URL +"/items/"+seed.toString())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk();
     //Then
    StepVerifier.create(itemRepository.findById(seed))
    .verifyComplete();
  }

  @Test
  public void Update(){
    //Given
    final UUID seed = UUID.randomUUID();
    itemRepository.save(ItemFactory.create(seed).build()).block();
    final UpdateItemDto updateItemDto = UpdateItemDtoFactory.create(seed)
        .price(2D).build();

    final Item expected = ItemFactory.create(seed).price(2D).build();
    //When
    final EntityExchangeResult<Item> actual = webTestClient.put()
        .uri(URL +"/items")
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(updateItemDto)
        .exchange()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectStatus()
        .isOk()
        .expectBody(Item.class)
        .returnResult();
    //Then
    StepVerifier.create(itemRepository.findById(seed))
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  public void failUpdate_withNotFound(){
    //Given
    final UUID seed = UUID.randomUUID();
    final UpdateItemDto updateItemDto = UpdateItemDtoFactory.create(seed)
        .price(2D).build();
    final ErrorDto expected = ErrorDto.builder()
        .errorCode(1)
        .msg("Item with Id: "+seed+", was not found")
        .build();
    //When
    final EntityExchangeResult<ErrorDto> actual = webTestClient.put()
        .uri(URL +"/items")
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(updateItemDto)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody(ErrorDto.class)
        .returnResult();

    //Then
    assertThat(actual.getResponseBody())
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }
}
