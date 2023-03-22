package com.example.payservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "pay_users")
public class UserEntity extends BaseEntity {
    @Id
    @Column(name = "USER_ID", unique = true)
    private Long userId;

    private Integer deposit;
    private Integer prize;
}
