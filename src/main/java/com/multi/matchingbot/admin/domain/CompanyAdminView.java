package com.multi.matchingbot.admin.domain;

import com.multi.matchingbot.common.domain.dto.BaseAuditDto;
import com.multi.matchingbot.common.domain.enums.Yn;
import lombok.Data;

@Data
public class CompanyAdminView extends BaseAuditDto {
    private Long id;
    private String formattedId;
    private String name;
    private String email;
    private String phone;
    private String businessNo;
//    private String maskedNo;
    private String address;
    private String industry;
    private boolean agreementValid;
    private Yn reportStatus;
    private Yn status;

    public String getStatusText() {
        return status == Yn.Y ? "ACTIVE" : "INACTIVE";
    }

    public String getStatusClass() {
        return status == Yn.Y ? "status-active" : "status-inactive";
    }


}
