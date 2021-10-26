package com.yl.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.yl.common.lang.Const;
import com.yl.common.lang.Result;
import com.yl.common.log.MyLog;
import com.yl.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

@RestController
public class AuthController extends BaseController{

	@Autowired
	Producer producer;


	@MyLog(value = "获取验证码")
	@GetMapping("/captcha")
	public Result captcha() throws IOException {

		String key = UUID.randomUUID().toString();
		String code = producer.createText();

		// 为了测试
//		key = "aaaaa";
//		code = "11111";

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


}
