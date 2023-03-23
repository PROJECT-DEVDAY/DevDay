package com.example.payservice.dto.nhbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class RequestTransfer {
    @NotNull
    @JsonProperty("Header")
    private Header header;
    @Size(min = 3, max = 3, message = "은행코드는 3자리 입니다.")
    @JsonProperty("Bncd")
    private String bncd;
    @NotBlank(message = "계좌번호를 입력해주세요.")
    @JsonProperty("Acno")
    private String acno;
    @NotBlank(message = "거래금액을 기입해주세요.")
    @JsonProperty("Tram")
    private String tram;
    @NotBlank(message = "출금계좌인자내용을 기입해주세요.")
    @JsonProperty("DractOtlt")
    private String dractOtlt;
    @NotBlank(message = "입금계좌인자내용을 기입해주세요.")
    @JsonProperty("MractOtlt")
    private String mractOtlt;
}
