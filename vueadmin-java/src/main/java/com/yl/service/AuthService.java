package com.yl.service;

import com.yl.model.dto.LoginUserDto;
import com.yl.model.dto.MailDto;
import com.yl.security.AccountUser;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * @author ：jerry
 * @date ：Created in 2024/7/1 09:39
 * @description：
 * @version: V1.1
 */
public interface AuthService {
    Map<Object,Object> login(LoginUserDto user);

    Boolean logout(String username);

    Map<Object, Object> captcha() throws IOException;


    Map<Object, Object> userInfo(Principal principal);

    Boolean sendMsg(MailDto mail);

    String refreshToken();


}
