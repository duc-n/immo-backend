package com.cele.immo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Autowired
    private Environment env;

    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(ReactiveMongoDatabaseFactory reactiveMongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        return new ReactiveGridFsTemplate(reactiveMongoDbFactory, mappingMongoConverter);
    }

    @Autowired
    public void setMongoConverter(MappingMongoConverter mongoConverter) {
        // remove the "_class" : "com.cele.Bien"
        /*
        mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        List<Object> customConverters = new ArrayList<Object>();
        customConverters.add(new ConsultantDTOReaderConverter());
        mongoConverter.setCustomConversions(new MongoCustomConversions(customConverters));
        mongoConverter.afterPropertiesSet();*/
    }

/*
    @Bean
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(new MongoClientURI(env.getProperty("spring.data.mongodb.uri")));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

        return mongoTemplate;

    }
*/
    /*@Bean
    public CascadeSaveMongoEventListener userCascadingMongoEventListener() {
        return new CascadeSaveMongoEventListener();
    }*/
}
