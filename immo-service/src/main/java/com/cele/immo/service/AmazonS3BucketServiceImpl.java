package com.cele.immo.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class AmazonS3BucketServiceImpl implements AmazonS3BucketService {

    private AmazonS3 amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;

    @PostConstruct
    private void initializeAmazon() {
        log.debug("Init Amazon S3");
        amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
    }

    @Override
    public Mono<String> uploadFile(String userName, String bienId, FilePart filePart) {
        log.debug("UserName: {}", userName);

        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    String filename = filePart.filename();
                    String key = userName + "/" + bienId + "/" + filename;
                    log.debug("UploadFile key : {} - Data Length :{} - UserName :{}", key, dataBuffer.capacity());

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(dataBuffer.capacity());
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, dataBuffer.asInputStream(), metadata);

                    amazonS3.putObject(putObjectRequest
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    return filename;
                });
    }

    @Override
    public Mono<Void> deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, "14257/47857/" + fileName));
        return Mono.empty();
    }


}
