package com.multi.matchingbot.common.domain.enums;

public enum Gender {
    M, F;

    public String getLabel() {
        return this == M ? "남자" : "여자";
    }
}
