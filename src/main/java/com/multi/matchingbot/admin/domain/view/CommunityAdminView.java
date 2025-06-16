package com.multi.matchingbot.admin.domain.view;

import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import com.multi.matchingbot.common.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommunityAdminView extends BaseAuditDto {

    private Long id;
    private String formattedId;
    private String title;
    private String categoryName;
    private Role writerType; // MEMBER 또는 COMPANY
    private String writerName;
    private int views;
}
