package com.multi.matchingbot.attachedItem.domain;

import com.multi.matchingbot.common.domain.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileMeta {
    private Long referenceId;
    private ItemType itemType;
    private String originalName;
    private String systemName;
    private String path;
}
