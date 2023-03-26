package com.example.payservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalResponse<T> {
    private int code = 0;
    private String message = "success";
    private T data;

    public InternalResponse(T data) {
        this.data = data;
    }
}
