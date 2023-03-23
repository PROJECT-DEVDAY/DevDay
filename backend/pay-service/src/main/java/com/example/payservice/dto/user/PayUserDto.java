package com.example.payservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayUserDto {

    private Long userId;
    @Positive
    private Integer prize;
    @Positive
    private Integer deposit;

    public static PayUserDto createPayUserDto(long userId) {
        return new PayUserDto(userId, 0, 0);
    }
}
