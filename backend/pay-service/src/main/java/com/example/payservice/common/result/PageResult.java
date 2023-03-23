package com.example.payservice.common.result;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PageResult<T> extends Result {
    private Page<T> data;
}
