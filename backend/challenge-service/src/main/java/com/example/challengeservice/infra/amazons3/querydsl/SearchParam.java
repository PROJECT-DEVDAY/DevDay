package com.example.challengeservice.infra.amazons3.querydsl;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchParam {
    String type;
    String search;

    int size;
    Long offset;

    String nowDate;




}
