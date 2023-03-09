package com.furniture.backend.config;

import com.furniture.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/users/checkUsernameAvailability", "/api/users/checkEmailAvailability").permitAll()

				.anyRequest()
				.authenticated()
				/*
				 * Bởi vì filter của chúng ta active once per request, điều đó khiến mỗi request chúng ta gọi đều
				 * cần được authenticated. Vì vậy chúng ta không nên lưu trữ authentication state vào trong session.
				 * Điều đó giúp chúng ta đảm bảo rằng mỗi request đều sẽ được authenticated, và Spring sẽ tạo 1
				 * session mới cho mỗi request
				 */
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
