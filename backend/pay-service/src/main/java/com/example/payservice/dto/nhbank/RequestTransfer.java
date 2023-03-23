package com.example.payservice.dto.nhbank;

import com.example.payservice.dto.AccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.env.Environment;

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

    /**
     * 농협 전송 API body를 만듭니다.
     * @param env
     * @param header
     * @param accountDto
     * @param money
     * @return
     */
    public static RequestTransfer createNHApiRequestTransfer(
            Environment env,
            Header header,
            AccountDto accountDto,
            int money
    ) {
        return RequestTransfer.builder()
                .header(header)
                .bncd(accountDto.getBankCode())
                .acno(accountDto.getNumber())
                .tram(String.valueOf(money))
                .dractOtlt(env.getProperty("openapi.nonghyup.otlt") + " " + money + "원")
                .mractOtlt(env.getProperty("openapi.nonghyup.otlt") + " " + money + "원")
                .build();
    }
}
