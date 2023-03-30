package com.example.challengeservice.infra.querydsl;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchParam {
    String category;
    String search;
    int size;
    Long offset;
    String nowDate;

}
