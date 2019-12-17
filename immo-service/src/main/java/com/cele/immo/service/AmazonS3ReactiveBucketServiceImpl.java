package com.cele.immo.service;

import com.cele.immo.exception.CeleS3Exception;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class AmazonS3ReactiveBucketServiceImpl implements AmazonS3ReactiveBucketService {
    private S3AsyncClient amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;

    @PostConstruct
    private void initializeAmazon() {
        log.debug("Init Amazon S3 Reactive");
        Region region = Region.EU_WEST_2;
        amazonS3 = S3AsyncClient.builder().region(region).build();
    }

    @SneakyThrows
    @Override
    public Mono<String> uploadFile(FilePart filePart) {
        //Path path = Paths.get(filePart.filename());
        Path path = Paths.get("/Users/cele/Desktop/Workspace/immo-backend/pom.xml");

        File file = File.createTempFile("tmp", null);
        filePart.transferTo(file);// The content file is empty. Wait a solution.

        return Mono.fromFuture(
                amazonS3.putObject(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key("14257/47857/pom.xml")
                                .acl(ObjectCannedACL.PUBLIC_READ)
                                .build()
                        , AsyncRequestBody.fromFile(path)
                )
        )
                .onErrorMap(throwable -> new CeleS3Exception("S3 ingestion error", throwable))
                .doOnSuccess(s -> {
                    //file.deleteOnExit();
                    log.info("S3S in doOnSuccess - {}", s);
                })
                .map(putObjectResponse -> filePart.filename());

    }


    private AsyncRequestBody createAsyncBody(Flux<FilePart> part) {
        return new AsyncRequestBody() {
            @Override
            public Optional<Long> contentLength() {
                return Optional.empty(); // what value to return from here?
            }

            @Override
            public void subscribe(Subscriber<? super ByteBuffer> s) {
                part.flatMap(FilePart::content).map(toByteBuffer()).subscribe(s);
            }

            private Function<DataBuffer, ByteBuffer> toByteBuffer() {
                return (buffer) -> {
                    byte[] bytes = new byte[buffer.asByteBuffer().remaining()];
                    try {
                        return ByteBuffer.wrap(bytes);
                    } finally {
                        DataBufferUtils.release(buffer.read(bytes));
                    }
                };
            }
        };
    }

    @Override
    public String deleteFile(String fileName) {
        return null;
    }
}
