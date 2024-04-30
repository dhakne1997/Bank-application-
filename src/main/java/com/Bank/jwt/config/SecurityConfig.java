package com.Bank.jwt.config;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.AuthenticationProvider; 
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; 
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Bank.jwt.filter.JwtAuthFilter;
import com.Bank.jwt.service.UserInfoService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { 

	@Autowired
	private JwtAuthFilter authFilter; 

	// User Creation 
	@Bean
	public UserDetailsService userDetailsService() { 
		return new UserInfoService(); 
	} 
	


	// Configuring HttpSecurity 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		return http.csrf().disable() 
				.authorizeHttpRequests() 
				.requestMatchers("/auth/addNewUser", "/auth/generateToken").permitAll() 
	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**").permitAll()
				.requestMatchers("/bank/create").permitAll()
				.requestMatchers("/bank/update").permitAll()
	              .requestMatchers( "/bank/delete/**").permitAll()
	              .requestMatchers("/bank/accountId/**").permitAll()
	              .requestMatchers( "/bank/withdraw").permitAll()
	              .requestMatchers( "/bank/deposit").permitAll()
	              .requestMatchers( "/bank/transfer").permitAll()
	              .requestMatchers("/bank/balance/**").permitAll()
				.and() 
				.authorizeHttpRequests().requestMatchers("/auth/generateToken/**").authenticated() 
				.and() 
				.sessionManagement() 
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
				.and() 
				.authenticationProvider(authenticationProvider()) 
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) 
				.build(); 
	} 

	// Password Encoding 
	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder(); 
	} 

	@Bean
	public AuthenticationProvider authenticationProvider() { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
		authenticationProvider.setUserDetailsService(userDetailsService()); 
		authenticationProvider.setPasswordEncoder(passwordEncoder()); 
		return authenticationProvider; 
	} 

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
		return config.getAuthenticationManager(); 
	} 


} 
