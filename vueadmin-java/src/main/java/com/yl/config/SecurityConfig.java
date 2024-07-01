package com.yl.config;

import com.yl.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Autowired
	CaptchaFilter captchaFilter;

	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Autowired
	UserDetailServiceImpl userDetailService;


	@Autowired
	IgnoreWhitesConfig ignoreWhites;

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
		return jwtAuthenticationFilter;
	}


	/**
	 * 自定义密码处理
	 * */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new MyPasswordEncoder();
	}



	protected void configure(HttpSecurity http) throws Exception {

		http.cors()
				.configurationSource(corsConfigurationSource())        //跨域配置
				.and().csrf().disable()

				// 禁用session
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// 配置拦截规则
				.and()
				.authorizeRequests()
				.antMatchers( ignoreWhites.getWhites()).permitAll()
				.anyRequest().authenticated()

				// 异常处理器
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				// 配置自定义的过滤器
				.and()
				.addFilter(jwtAuthenticationFilter())
				.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());      //自定义密码规则处理
	}


	/**
	 * 跨域具体处理
	 */
	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.addAllowedOriginPattern("*");//替换这个
//		configuration.setAllowedOriginPatterns(Arrays.asList("*"));

		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}



	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


}
