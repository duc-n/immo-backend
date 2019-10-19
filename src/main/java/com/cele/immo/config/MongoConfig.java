package com.cele.immo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(ReactiveMongoDatabaseFactory reactiveMongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        return new ReactiveGridFsTemplate(reactiveMongoDbFactory, mappingMongoConverter);
    }

    /*@Bean
    public CascadeSaveMongoEventListener userCascadingMongoEventListener() {
        return new CascadeSaveMongoEventListener();
    }*/
}
