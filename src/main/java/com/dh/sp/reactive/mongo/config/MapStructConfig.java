package com.dh.sp.reactive.mongo.config;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring",
    imports = {java.util.UUID.class})
public interface MapStructConfig {

}
