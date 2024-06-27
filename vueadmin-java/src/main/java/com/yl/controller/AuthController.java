package com.yl.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.yl.common.CommonResultVo;
import com.yl.constant.Const;
import com.yl.model.dto.MailDto;
import com.yl.common.log.MyLog;
import com.yl.model.entity.User;
import com.yl.service.UserService;
import com.yl.utils.JwtUtils;
import com.yl.utils.MailUtil;
import com.yl.utils.RedisTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：用户信息
 * @version: V1.1
 */

@Api(tags = "用户信息管理")
@RestController
public class AuthController{

	@Resource
	Producer producer;

	@Resource
	private MailUtil mailUtil;			//邮箱工具类

	@Resource
	UserService userService;

	@Resource
	RedisTools redisTools;

	@Resource
	JwtUtils jwtUtils;

	@ApiOperation("获取验证码")
	@MyLog(value = "获取验证码")
	@GetMapping("/captcha")
	public CommonResultVo captcha() throws IOException {

		String key = UUID.randomUUID().toString();
		String code = producer.createText();

		BufferedImage image = producer.createImage(code);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", outputStream);

		BASE64Encoder encoder = new BASE64Encoder();
		String str = "data:image/jpeg;base64,";

		String base64Img = str + encoder.encode(outputStream.toByteArray());

		redisTools.hset(Const.CAPTCHA_KEY, key, code, 120);

		return CommonResultVo.success(
				MapUtil.builder()
						.put("token", key)
						.put("captchaImg", base64Img)
						.build()

		);
	}

	/**
	 * 获取用户信息接口
	 * @param principal
	 * @return
	 */
	@ApiOperation("获取用户信息接口")
	@GetMapping("/sys/userInfo")
	public CommonResultVo userInfo(Principal principal) {

		User sysUser = userService.getByUsername(principal.getName());

		return CommonResultVo.success(MapUtil.builder()
				.put("id", sysUser.getId())
				.put("username", sysUser.getUsername())
				.put("avatar", sysUser.getAvatar())
				.put("createTime", sysUser.getCreateTime())
				.map()
		);
	}


	/**
	 * 邮件发送接口
	 * */
	@ApiOperation("邮件发送接口")
	@PostMapping("/send")
	public CommonResultVo sendMsg(@RequestBody MailDto mail){
		try {
			mailUtil.sendSimpleMail(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonResultVo.success("");
	}

	@ApiOperation("token续期")
	@GetMapping("/refresh-token")
	public CommonResultVo refreshToken() {
		// 获取当前认证用户的信息
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 根据认证用户的信息生成新的Token
		String newJwt = jwtUtils.generateToken(authentication.getName());
		return CommonResultVo.success(newJwt);
	}


}
