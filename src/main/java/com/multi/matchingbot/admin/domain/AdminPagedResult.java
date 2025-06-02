package com.multi.matchingbot.admin.domain;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.IntStream;

@Getter
public class AdminPagedResult<T>{
    private final Page<T> page;
    private final List<Integer> pageNumbers;
    private final int currentPage;

    public AdminPagedResult(Page<T> page) {
        this.page = page;
        this.currentPage = page.getNumber();
        this.pageNumbers = IntStream.range(0, page.getTotalPages()).boxed().toList();
    }

    public List<T> getContent() {
        return page.getContent();
    }
}
