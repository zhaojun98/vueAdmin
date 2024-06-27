package com.yl.security;


import com.yl.common.exception.CustomException;
import com.yl.utils.SignUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Cipher;

/**
 * @author ：jerry
 * @date ：Created in 2024/6/16 10:01
 * @description：自定义密码处理
 * @version: V1.1
 */
public class MyPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value(value = "${rsa.privateKey}")
    private String privateKey;

    public MyPasswordEncoder() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    /**
     * 密码对比,对比前,可以先解密密文
     * */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //解析处理
        try {
            //todo 如果需要前端对登陆密码加密,请用如下代码

//            String passwordStr = SignUtil.encryptByprivateKey(rawPassword.toString(), privateKey, Cipher.DECRYPT_MODE);
//            return bCryptPasswordEncoder.matches(passwordStr, encodedPassword);

            return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            throw new CustomException("登陆密码错误:"+e.getMessage());
        }

    }


}
