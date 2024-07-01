package com.yl.constant;

/**
 * @author ：jerry
 * @date ：Created in 2024/7/1 09:20
 * @description：
 * @version: V1.1
 */
public interface JwtConstant {
    /**
     * 用户ID字段
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * 登录用户
     */
    String LOGIN_USER = "login_user";

    /**
     * 用户标识
     */
    String USER_KEY = "user_key";

    /**
     * 用户名字段
     */
    String DETAILS_USERNAME = "username";

    /**
     * token有效时间
     */
    String EXPIRE = "expire";

    /**
     * 刷新token有效时间
     */
    String REFRESH_TOKEN_VALIDITY_TIME = "refresh_token_validity_time";

    /**刷新token*/
    String REFRESH_TOKEN= "REFRESH_TOKEN";
}
