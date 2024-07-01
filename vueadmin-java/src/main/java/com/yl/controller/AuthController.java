package com.yl.controller;

import com.yl.common.CommonResultVo;
import com.yl.common.log.MyLog;
import com.yl.model.dto.LoginUserDto;
import com.yl.model.dto.MailDto;
import com.yl.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.Principal;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：用户信息
 * @version: V1.1
 */

@Api(tags = "登陆管理")
@RestController
public class AuthController{

	@Resource
	private AuthService authenticate;


	@ApiOperation("登陆")
	@MyLog(value = "登陆")
	@PostMapping("/login")
	public CommonResultVo login(@RequestBody LoginUserDto user) throws IOException {
		return CommonResultVo.success(authenticate.login(user));
	}

	@ApiOperation("退出登陆")
	@MyLog(value = "退出登陆")
	@GetMapping("/logout")
	public CommonResultVo logout(String username) throws IOException {
		return CommonResultVo.success(authenticate.logout(username));
	}


	@ApiOperation("获取验证码")
	@MyLog(value = "获取验证码")
	@GetMapping("/captcha")
	public CommonResultVo captcha() throws IOException {
		return CommonResultVo.success(authenticate.captcha());
	}

	/**
	 * 获取用户信息接口
	 * @param principal
	 * @return
	 */
	@ApiOperation("获取用户信息接口")
	@GetMapping("/sys/userInfo")
	public CommonResultVo userInfo(Principal principal) {
		return CommonResultVo.success(authenticate.userInfo(principal));
	}


	/**
	 * 邮件发送接口
	 * */
	@ApiOperation("邮件发送接口")
	@PostMapping("/send")
	public CommonResultVo sendMsg(@RequestBody MailDto mail){
		return CommonResultVo.success(authenticate.sendMsg(mail));
	}

	@ApiOperation("token续期")
	@GetMapping("/refresh-token")
	public CommonResultVo refreshToken() {
		return CommonResultVo.success(authenticate.refreshToken());
	}


}
