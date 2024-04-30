//package com.Bank.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public  class SecurityConfiguration {
//
//	@Value("${spring.security.user.name}")
//	String username;
//	
//	@Value("${spring.security.user.password}")
//	String userpassword;
//	
//	@Value("${spring.security.admin.name}")
//	String adminuser;
//	
//	@Value("${spring.security.admin.password}")
//	String adminpassword;
//	
//	  @Bean
//      public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//          UserDetails user = User.withUsername(username)
//              .password(passwordEncoder.encode(userpassword))
//              .roles("USER")
//              .build();
//
//          UserDetails admin = User.withUsername(adminuser)
//              .password(passwordEncoder.encode(adminpassword))
//              .roles("USER", "ADMIN")
//              .build();
//
//          return new InMemoryUserDetailsManager(user, admin);
//      }
//
//	  @Bean
//	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	      http.authorizeRequests(request -> request
//	              .requestMatchers(HttpMethod.POST, "/bank/create").hasRole("ADMIN")
//	              .requestMatchers(HttpMethod.PUT, "/bank/update").hasRole("ADMIN")
//	              .requestMatchers(HttpMethod.DELETE, "/bank/delete/**").hasRole("ADMIN")
//	              .requestMatchers(HttpMethod.GET, "/bank/accountId/**").hasRole("USER")
//	              .requestMatchers(HttpMethod.POST, "/bank/withdraw").hasRole("USER")
//	              .requestMatchers(HttpMethod.POST, "/bank/deposit").hasRole("USER")
//	              .requestMatchers(HttpMethod.POST, "/bank/transfer").hasRole("USER")
//	              .requestMatchers(HttpMethod.GET, "/bank/balance/**").hasRole("USER")
//	              .anyRequest().authenticated())
//	          .httpBasic(Customizer.withDefaults())
//	          .csrf().disable() 
//	          .formLogin().disable(); 
//	      return http.build();
//	  }
//
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//}
