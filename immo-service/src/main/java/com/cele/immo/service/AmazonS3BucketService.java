package com.cele.immo.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface AmazonS3BucketService {
    public Mono<String> uploadFile(String userName, String bienId, FilePart filePart);

    Mono<Void> deleteFile(String fileName);

}
