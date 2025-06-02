package com.multi.matchingbot.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkResponseDto {
    private boolean isSuccess;
    private List<Long> failedIds;
}
