package com.example.helper.config;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.helper.common.CommonUtils;
import com.example.helper.common.CommonUtils.UserDetailPrincipal;

import io.jsonwebtoken.security.SignatureException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CommonUtils commonUtils;
	
	@Bean
	public RestAuthenticationEntryPoint restServicesEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		TokenAuthenticationFilter tokenAuthenticationFilter=new TokenAuthenticationFilter(commonUtils);
		httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity.antMatcher("/api/lchsdt/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint()).and()
		.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
		
		httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers(HttpMethod.POST,"/api/lchsdt/authenticate/login").permitAll()
//				.anyRequest().authenticated();
				.anyRequest().permitAll();
	}

	public class TokenAuthenticationFilter implements Filter {
		private CommonUtils commonUtils;
		

		public TokenAuthenticationFilter(CommonUtils commonUtils) {
			super();
			this.commonUtils = commonUtils;
		}

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
		}
		
		@Override
		public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
				throws IOException, ServletException {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			String accessToken = httpRequest.getHeader("Authorization");
			if (accessToken != null&&accessToken.isEmpty()==false) {
				try {
					UserDetailPrincipal userDetail = this.commonUtils.getUserInfo(accessToken);
					
					boolean authenticated = userDetail.isAuthenticated();
					if (authenticated == true) {
						List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
						final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetail, null, grantedAuthorityList);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					} 
				} catch (SignatureException e) {
					System.out.println("Convert JWT error:"+accessToken+":"+httpRequest.getPathInfo());
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			chain.doFilter(request, response);
		}

		@Override
		public void destroy() {

		}
	}
}