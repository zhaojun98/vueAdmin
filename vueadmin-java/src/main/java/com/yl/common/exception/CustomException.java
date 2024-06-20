package com.yl.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

/**
 * @Author GoryLee
 * @Date 2022/12/7 21:40
 * @Version 1.0
 */
@Getter
@Setter
public class CustomException extends AuthenticationException {

    private String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

}
