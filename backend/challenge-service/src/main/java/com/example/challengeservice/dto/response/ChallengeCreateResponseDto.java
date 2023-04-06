package com.example.challengeservice.dto.response;


import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class ChallengeCreateResponseDto {
    private Long id;

    private String message;

    public static ChallengeCreateResponseDto from(Long id, String message){
        return ChallengeCreateResponseDto.builder()
                .id(id)
                .message(message)
                .build();
    }
}
