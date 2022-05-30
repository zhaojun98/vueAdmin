package com.yl.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.yl.common.dto.MailDto;
import com.yl.common.lang.Const;
import com.yl.common.lang.Result;
import com.yl.common.log.MyLog;
import com.yl.entity.User;
import com.yl.utils.MailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

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
public class AuthController extends BaseController{

	@Autowired
	Producer producer;

	@Autowired
	private MailUtil mailUtil;			//邮箱工具类

	@ApiOperation("获取验证码")
	@MyLog(value = "获取验证码")
	@GetMapping("/captcha")
	public Result captcha() throws IOException {

		String key = UUID.randomUUID().toString();
		String code = producer.createText();

		BufferedImage image = producer.createImage(code);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", outputStream);

		BASE64Encoder encoder = new BASE64Encoder();
		String str = "data:image/jpeg;base64,";

		String base64Img = str + encoder.encode(outputStream.toByteArray());

		redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);

		return Result.succ(
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
	public Result userInfo(Principal principal) {

		User sysUser = sysUserService.getByUsername(principal.getName());

		return Result.succ(MapUtil.builder()
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
	public Result sendMsg(@RequestBody MailDto mail){
		try {
			mailUtil.sendSimpleMail(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.succ("");
	}


}
