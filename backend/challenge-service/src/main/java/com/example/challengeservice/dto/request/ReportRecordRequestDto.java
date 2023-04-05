package com.example.challengeservice.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRecordRequestDto {


    private Long challengeRecordId;

    private Long challengeRoomId;

    private String reportDate;

}
