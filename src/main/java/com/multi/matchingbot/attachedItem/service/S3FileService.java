package com.multi.matchingbot.attachedItem.service;

import com.multi.matchingbot.attachedItem.AttachedItemRepository;
import com.multi.matchingbot.attachedItem.AwsS3Utils;
import com.multi.matchingbot.attachedItem.FileMetaConverter;
import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.attachedItem.domain.FileMeta;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService {

    private final AwsS3Utils awsS3Utils; // 기존 S3Operations 기반 유틸 사용
    private final AttachedItemRepository attachedItemRepository;
    private final CompanyRepository companyRepository;

    /**
     * BufferedImage → InputStream 변환 후 S3 업로드 + DB 기록
     */
    public AttachedItem save(FileMeta meta, BufferedImage image) {
        try {
            // 0. 중복 체크
            Optional<AttachedItem> existingOpt = attachedItemRepository
                                .findByReferenceIdAndItemTypeAndStatus(meta.getReferenceId(), meta.getItemType(), Yn.Y);
            String fileName = existingOpt.map(AttachedItem::getSystemName).orElse(meta.getSystemName());

            // 1. BufferedImage → InputStream
            InputStream inputStream = bufferedImageToInputStream(image, "jpg");
            // 2. S3 경로 설정 (upload/company/42/report_full.jpg 등)
            String s3Dir = extractDirFromPath(meta.getPath());  // ex: upload/company/42/
            // 3. 업로드
            String uploadedName = awsS3Utils.uploadInputStream(s3Dir, fileName, inputStream, "image/jpeg");

            // 4. DB 저장
            AttachedItem entity = FileMetaConverter.toEntity(meta, Yn.Y);
            entity.setSystemName(uploadedName);
            entity.setOriginalName(meta.getOriginalName());
            entity.setPath("https://" + awsS3Utils.getBucketUrl() + "/" + s3Dir + uploadedName);
            Company company = companyRepository.getCompanyById(entity.getReferenceId());
            company.setReportStatus(Yn.Y);
            companyRepository.save(company);

            existingOpt.ifPresent(existing -> entity.setId(existing.getId()));
            return attachedItemRepository.save(entity);
        } catch (Exception e) {
            log.error("S3 이미지 저장 실패", e);
            throw new RuntimeException("S3 저장 중 오류 발생", e);
        }
    }

    private InputStream bufferedImageToInputStream(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    private String extractDirFromPath(String fullPath) {
        int lastSlash = fullPath.lastIndexOf('/');
        return fullPath.substring(0, lastSlash + 1);
    }
}

