package com.multi.matchingbot.attachedItem.service;

import com.multi.matchingbot.attachedItem.AttachedItemRepository;
import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.attachedItem.domain.FileMeta;
import com.multi.matchingbot.attachedItem.mapper.FileMetaConverter;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AttachedItemRepository attachedItemRepository;

    /**
     * 이미지 저장 + DB 기록
     *
     * @param meta  파일 메타 정보
     * @param image BufferedImage 이미지
     * @return 저장된 AttachedItem 엔티티
     */
    public AttachedItem save(FileMeta meta, BufferedImage image) {
        try {
            Optional<AttachedItem> existingOpt = attachedItemRepository
                    .findByReferenceIdAndItemTypeAndStatus(meta.getReferenceId(), meta.getItemType(), Yn.Y);

            String systemName;
            if (existingOpt.isPresent()) {
                systemName = existingOpt.get().getSystemName();
            } else {
                systemName = meta.getSystemName();
            }

            String fullPath = Paths.get("src/main/resources/static", meta.getPath()).toString();
            File file = new File(fullPath);
            File parentDir = file.getParentFile();

            if (!parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created)
                    log.info("새 디렉토리 생성: {}", parentDir);
            }

            ImageIO.write(image, "png", file);
            log.info("이미지 저장 완료: {}", file.getAbsolutePath());

            AttachedItem entity = FileMetaConverter.toEntity(meta, Yn.Y);
            entity.setSystemName(systemName);
            entity.setOriginalName(meta.getOriginalName());
            entity.setPath(meta.getPath());

            if (existingOpt.isPresent())
                entity.setId(existingOpt.get().getId());

            return attachedItemRepository.save(entity);
            /*
            AttachedItem entity = FileMetaConverter.toEntity(meta, Yn.Y);
            return attachedItemRepository.save(entity);*/
        } catch (Exception e) {
            log.error("!! 파일 저장 or DB 업로드 실패");
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }
}
