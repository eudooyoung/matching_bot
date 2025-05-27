package com.multi.matchingbot.common.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseAuditDto {
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
