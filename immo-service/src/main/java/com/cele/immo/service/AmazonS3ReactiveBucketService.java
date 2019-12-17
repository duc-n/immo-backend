package com.cele.immo.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface AmazonS3ReactiveBucketService {
    Mono<String> uploadFile(FilePart filePart);

    String deleteFile(String fileName);

}
