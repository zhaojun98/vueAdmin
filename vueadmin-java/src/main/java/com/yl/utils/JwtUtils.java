package com.yl.utils;

import com.yl.constant.JWTConstant;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {

    /**
     * 有效时间(单位秒)
     */
    private long expire;

    /**
     * 令牌秘钥
     */
    private String secret;

    /**
     * token请求头key
     */
    private String header;

    /**
     * 生成jwt
     *
     * @param username 用户名
     * @return
     */
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)// 7天過期
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析jwt
     *
     * @param jwt
     * @return
     */
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * jwt是否过期
     *
     * @param claims
     * @return
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, JWTConstant.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, JWTConstant.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, JWTConstant.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public String getUserId(Claims claims) {
        return getValue(claims, JWTConstant.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUserName(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, JWTConstant.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public String getUserName(Claims claims) {
        return getValue(claims, JWTConstant.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        Object value = claims;
        String defaultValue = key;

        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

}
