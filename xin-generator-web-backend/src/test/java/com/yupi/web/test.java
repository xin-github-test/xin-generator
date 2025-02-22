package com.yupi.web;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

public class test {


    @Test
    public void test01 () {
        String encryptPassword = DigestUtils.md5DigestAsHex(("xin" + "12345678").getBytes());
        System.out.println(encryptPassword);
    }
}
