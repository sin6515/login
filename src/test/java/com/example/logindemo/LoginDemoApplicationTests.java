package com.example.logindemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class LoginDemoApplicationTests {
    @Test
    public void contextLoads() {
        log.trace("这是trace");
        log.debug("这是debug");
        log.info("这是info");
        log.warn("这是warn");
        log.error("这是error");
    }

}
