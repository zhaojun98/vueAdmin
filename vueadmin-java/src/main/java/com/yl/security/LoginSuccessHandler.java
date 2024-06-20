package com.yl.security;

import cn.hutool.json.JSONUtil;
import com.yl.common.CommonResultVo;
import com.yl.model.entity.Log;
import com.yl.service.LogService;
import com.yl.utils.IpUtiles;
import com.yl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private LogService logService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = response.getOutputStream();

		// 生成jwt，并放置到请求头中
		String jwt = jwtUtils.generateToken(authentication.getName());
		AccountUser principal = (AccountUser)authentication.getPrincipal();
		Map<String, Object> map = new HashMap<>();
		map.put("username",principal.getUsername());
		map.put("Authorization",jwt);

		response.setHeader(jwtUtils.getHeader(), jwt);
		Log sysLog=new Log();
		sysLog.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		sysLog.setCreateTime(LocalDateTime.now());
		sysLog.setIp(IpUtiles.getRealIp(request));
		sysLog.setOperation("登陆成功");
		sysLog.setMethod("login");
		logService.save(sysLog);

		outputStream.write(JSONUtil.toJsonStr(CommonResultVo.success(map)).getBytes("UTF-8"));
		outputStream.flush();
		outputStream.close();
	}

}
