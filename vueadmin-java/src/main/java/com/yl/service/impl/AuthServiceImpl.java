package com.yl.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.yl.constant.Const;
import com.yl.constant.JwtConstant;
import com.yl.constant.RedisKeyConstant;
import com.yl.model.dto.LoginUserDto;
import com.yl.model.dto.MailDto;
import com.yl.model.entity.User;
import com.yl.security.AccountUser;
import com.yl.service.AuthService;
import com.yl.service.UserService;
import com.yl.utils.JwtUtils;
import com.yl.utils.MailUtil;
import com.yl.utils.RedisTools;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

/**
 * @author ：jerry
 * @date ：Created in 2024/7/1 09:40
 * @description：
 * @version: V1.1
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    Producer producer;

    @Resource
    private MailUtil mailUtil;            //邮箱工具类

    @Resource
    UserService userService;

    @Resource
    RedisTools redisTools;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    private AuthenticationManager authenticationManager;


    /**
     * 自定义登陆接口
     *
     * @param user
     * @return
     */
    @Override
    public Map<Object, Object> login(LoginUserDto user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ajax
        AccountUser principal = (AccountUser) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(authentication.getName());

        return MapUtil.builder()
                .put(JwtConstant.DETAILS_USER_ID, principal.getUserId())
                .put(JwtConstant.DETAILS_USERNAME, principal.getUsername())
                .put(jwtUtils.getHeader(), jwt)
                .put(JwtConstant.EXPIRE, jwtUtils.getExpire())
                .put(JwtConstant.REFRESH_TOKEN_VALIDITY_TIME, jwtUtils.getRefreshTokenValidityTime())
                .put(JwtConstant.REFRESH_TOKEN, jwtUtils.generateRefreshToken(authentication.getName()))
                .build();
    }

    @Override
    public Boolean logout(String username) {
        redisTools.del(RedisKeyConstant.GrantedAuthority + username);
        return true;
    }


    /**
     * 获取验证码
     *
     * @return
     * @throws IOException
     */
    @Override
    public Map<Object, Object> captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisTools.hset(Const.CAPTCHA_KEY, key, code, 120);

        return MapUtil.builder()
                .put("token", key)
                .put("captchaImg", base64Img)
                .build();

    }

    /**
     * 获取用户信息
     *
     * @param principal
     * @return
     */
    @Override
    public Map<Object, Object> userInfo(Principal principal) {
        User sysUser = userService.getByUsername(principal.getName());
        return MapUtil.builder()
                .put("id", sysUser.getId())
                .put("username", sysUser.getUsername())
                .put("avatar", sysUser.getAvatar())
                .put("createTime", sysUser.getCreateTime())
                .map();
    }

    @Override
    public Boolean sendMsg(MailDto mail) {
        return mailUtil.sendSimpleMail(mail);
    }

    @Override
    public String refreshToken() {
        // 获取当前认证用户的信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 根据认证用户的信息生成新的Token
        return jwtUtils.generateToken(authentication.getName());
    }
}
