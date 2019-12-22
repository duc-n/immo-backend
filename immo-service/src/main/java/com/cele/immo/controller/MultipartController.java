package com.cele.immo.controller;

import com.cele.immo.dto.FileName;
import com.cele.immo.function.TriFunction;
import com.cele.immo.model.UserAccount;
import com.cele.immo.service.AmazonS3BucketService;
import com.google.common.collect.ImmutableMap;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.security.Principal;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping(value = "/multipart")
@RequiredArgsConstructor
@Slf4j
public class MultipartController {

    private final ReactiveGridFsTemplate gridFsTemplate;

    @Autowired
    AmazonS3BucketService amazonS3BucketService;

    public static <T1, T2, T3, R> Function<Tuple3<T1, T2, T3>, R> triFunctionOnTuple(TriFunction<T1, T2, T3, R> combinator) {
        return t3 -> combinator.apply(t3.getT1(), t3.getT2(), t3.getT3());
    }

    @PostMapping()
    public Mono<ResponseEntity> upload(@RequestPart Mono<FilePart> fileParts, @AuthenticationPrincipal UserAccount user) {
        log.debug("upload - file name. UserName {}", user.getUsername());

        DBObject metaData = new BasicDBObject();
        metaData.put("user", "duc");
        metaData.put("type", "image");
        metaData.put("parentId", "id");

        return fileParts
                .flatMap(part -> this.gridFsTemplate.store(part.content(), part.filename()))
                .map((id) -> ok().body(ImmutableMap.of("id", id.toHexString())));
    }

    @PostMapping(value = "/fs/{bienId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFs(@PathVariable String bienId, @RequestPart Mono<FilePart> fileParts, ServerWebExchange exchange) {

 /*
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                //.map(auth -> currentUser(auth))
                .zipWith(exchange.getFormData())
                .doOnNext(tuple -> {
                    // based on some input parameters, amend the current user data to be returned
                    log.debug("TA : {}", tuple.getT1());
                    log.debug("TA t2: {}", tuple.getT2());
                })
                .map(Tuple2::getT1);*/

        //Mono zip() => create tuble2, 3, 4....
        return Mono.zip(exchange.getPrincipal().map(Principal::getName).switchIfEmpty(Mono.empty()), Mono.just(bienId), fileParts)
                .flatMap(triFunctionOnTuple(amazonS3BucketService::uploadFile));

        // return fileParts
        //       .flatMap(part -> amazonS3BucketService.uploadFile(part))
        //     .map((id) -> ok().body(ImmutableMap.of("id", id)));

    }

    @PostMapping(value = "/delete")
    public Mono<Void> deleteFile(@RequestBody FileName fileName) {
        log.debug("Delete fileName:{}", fileName.getFileName());

        return amazonS3BucketService.deleteFile(fileName.getFileName());
    }


    @GetMapping("{id}")
    public Flux<Void> read(@PathVariable String id, ServerWebExchange exchange) {
        return this.gridFsTemplate.findOne(query(where("_id").is(id)))
                .log()
                .flatMap(gridFsTemplate::getResource)
                .flatMapMany(r -> exchange.getResponse().writeWith(r.getDownloadStream()));
    }

}
