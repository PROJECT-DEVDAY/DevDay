package com.example.payservice.service;

import com.example.payservice.service.prize.PrizeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PrizeServiceTest {

    @Autowired
    private PrizeService prizeService;

    @Test
    public void 상금_이체_테스트하기() throws Exception {

    }
}
