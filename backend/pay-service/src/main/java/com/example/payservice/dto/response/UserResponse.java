package com.example.payservice.dto.response;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserResponse {
    @NotNull
    Long userId;
    @NotNull
    String name;
    @NotNull
    @Email
    String email;
}
