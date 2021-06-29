package com.dh.sp.reactive.mongo.dto;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UpdateItemDto {
  UUID id;
  String description;
  Double price;
}
