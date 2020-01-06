package com.cele.immo.service;

import com.cele.immo.dto.S3FileDescription;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface AmazonS3BucketService {
    public Mono<S3FileDescription> uploadFile(String userName, String bienId, FilePart filePart);

    Mono<Void> deleteFile(String fileName);

}
