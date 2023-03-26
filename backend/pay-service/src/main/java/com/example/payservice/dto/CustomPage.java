package com.example.payservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomPage<T> {
    // 데이터
    private List<T> content;
    // 현재 페이지
    private int page;
    // 전체 페이지 수
    private int totalPages;
    // 전체 개수
    private long totalElements;
    // 사이즈 수
    private long size;
    // 첫장인지 여부
    private boolean first;
    // 마지막장인지 여부
    private boolean last;
    // 비어있는지 여부
    private boolean empty;

}
