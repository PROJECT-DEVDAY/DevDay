package com.example.payservice.vo.internal;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ResponseUser {
    @NotNull
    Long userId;
    @NotNull
    String name;
    @NotNull
    @Email
    String email;
}
