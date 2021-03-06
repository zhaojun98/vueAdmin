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
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Autowired
	LoginSuccessHandler loginSuccessHandler;

	@Autowired
	CaptchaFilter captchaFilter;

	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Autowired
	UserDetailServiceImpl userDetailService;

	@Autowired
	JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
		return jwtAuthenticationFilter;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final String[] URL_WHITELIST = {

			"/login",
			"/logout",
			"/captcha",
			"/favicon.ico",
			"/webjars/**",
			"/img.icons/**",
			"/swagger-resources/**",
			"/v3/api-docs/**",
			"/swagger-ui/**",
			"/doc.html",


	};


	protected void configure(HttpSecurity http) throws Exception {
		CorsConfigurer<HttpSecurity> httpSecurityLogoutConfigurer = http.cors().configurationSource(CorsConfigurationSource());
//		http.cors().configurationSource(CorsConfigurationSource()

		http.cors().and().csrf().disable()

				// ????????????
				.formLogin()
				.successHandler(loginSuccessHandler)
				.failureHandler(loginFailureHandler)

				.and()
				.logout()
				.logoutSuccessHandler(jwtLogoutSuccessHandler)

				// ??????session
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// ??????????????????
				.and()
				.authorizeRequests()
				.antMatchers(URL_WHITELIST).permitAll()
				.anyRequest().authenticated()

				// ???????????????
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				// ???????????????????????????
				.and()
				.addFilter(jwtAuthenticationFilter())
				.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);


	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
	}

	private CorsConfigurationSource CorsConfigurationSource() {
		CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");	//???????????????*????????????????????????????????????????????????ip???????????????????????????localhost???8080?????????????????????????????????
		corsConfiguration.addAllowedHeader("*");//header???????????????header????????????????????????token???????????????*?????????token???
		corsConfiguration.addAllowedMethod("*");	//????????????????????????PSOT???GET???
		((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration); //???????????????????????????url
		return source;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(
				"classpath:/static/");
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
	}
}
