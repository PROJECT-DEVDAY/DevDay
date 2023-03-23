package com.example.payservice.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import javax.persistence.Column;
import javax.persistence.Embeddable;
@Getter
@Setter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    @Column(name = "bank_code")
    @Comment("은행 코드")
    private String bankCode;

    @Comment("은행 계좌번호")
    private String number;

    @Comment("예금주")
    private String depositor;
}
