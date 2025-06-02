package com.multi.matchingbot.admin.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkRequestDto {

    @NotEmpty(message = "ID 목록이 비어 있을 수 없습니다.")
    private List<Long> ids;
}
