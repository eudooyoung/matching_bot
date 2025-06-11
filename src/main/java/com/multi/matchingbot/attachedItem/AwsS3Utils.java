package com.multi.matchingbot.attachedItem;


import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Utils {

    private final S3Operations s3Operations;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * InputStream 파일 업로드 (BufferedImage 지원용)
     */
    public String uploadInputStream(String dirName, String fileName, InputStream inputStream, String contentType) {
        String s3Key = dirName + fileName;
        try {
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType(contentType)
                    .build();

            s3Operations.upload(bucket, s3Key, inputStream, metadata);

            PutObjectAclRequest aclRequest = PutObjectAclRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObjectAcl(aclRequest);
            log.info("✅ S3 업로드 완료: {}", s3Key);

            return fileName;
        } catch (Exception e) {
            log.error("❌ S3 업로드 실패: {}", s3Key, e);
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    /**
     * S3 파일 삭제
     */
    public boolean deleteFile(String dirName, String fileName) {
        String s3Key = dirName + fileName;
        try {
            s3Operations.deleteObject(bucket, s3Key);
            log.info("🗑️ S3 삭제 완료: {}", s3Key);
            return true;
        } catch (Exception e) {
            log.error("❌ S3 삭제 실패: {}", s3Key, e);
            return false;
        }
    }

    /**
     * 버킷 URL 반환 (ex: matchingbot-company-reports.s3.ap-northeast-2.amazonaws.com)
     */
    public String getBucketUrl() {
        return bucket + ".s3.us-east-2.amazonaws.com";
    }
}
