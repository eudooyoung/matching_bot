package com.multi.matchingbot.attachedItem;

import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.attachedItem.domain.FileMeta;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class FileMetaConverter {

    public static AttachedItem toEntity(FileMeta meta, Yn status) {
        return AttachedItem.builder()
                .referenceId(meta.getReferenceId())
                .itemType(meta.getItemType())
                .originalName(meta.getOriginalName())
                .systemName(meta.getSystemName())
                .path(meta.getPath())
                .status(status)
                .build();
    }
}
