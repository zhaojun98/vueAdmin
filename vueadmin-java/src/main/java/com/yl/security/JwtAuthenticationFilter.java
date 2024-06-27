package com.yl.security;

import cn.hutool.core.util.StrUtil;
import com.yl.model.entity.User;
import com.yl.service.UserService;
import com.yl.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailServiceImpl userDetailService;

	@Autowired
    UserService sysUserService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String jwt = request.getHeader(jwtUtils.getHeader());
		if (StrUtil.isBlankOrUndefined(jwt)) {
			chain.doFilter(request, response);
		} else {
			// 1.验证Token是否有效
			Claims claim = jwtUtils.getClaimByToken(jwt);
			if (Objects.nonNull(claim)) {


				String username = claim.getSubject();
				// 获取用户的权限等信息

				User sysUser = sysUserService.getByUsername(username);
				UsernamePasswordAuthenticationToken token
						= new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

				SecurityContextHolder.getContext().setAuthentication(token);
				// 如果Token有效，则继续处理请求
				chain.doFilter(request, response);

			} else {
				// 2.如果Token无效，则返回特定状态码或抛出异常
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Token expired");
			}
		}
	}
}
