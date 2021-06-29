package com.dh.sp.reactive.mongo.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ErrorDto {
  String msg;
  Integer errorCode;
}
