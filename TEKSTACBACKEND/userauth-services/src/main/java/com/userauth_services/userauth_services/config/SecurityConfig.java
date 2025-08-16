package com.userauth_services.userauth_services.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.userauth_services.userauth_services.Model.DAO.ServiceImple.JwtService;
import com.userauth_services.userauth_services.filter.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JwtService jwtService;

	public SecurityConfig(JwtService jwtService) {
		super();
		this.jwtService = jwtService;
	}
	
	@Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService(), jwtService);
    }

	@Bean
	 public PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	 }
	 
	 // AuthorizationBlock
	 @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
        		// Public endpoints for authentication and token validation
        		.requestMatchers("/auth/save", "/auth/login", "/auth/Validatetoken").permitAll()
        		
        		// Secured endpoints for booking and vehicle operations, accessible by both USER and ADMIN
        		// This aligns with the new API paths.
        		.requestMatchers("/api/vehicles/**", "/api/bookings/**").hasAnyAuthority("ADMIN", "USER")
        		
        		
        		
        		// Any other request must be authenticated
        		.anyRequest().authenticated())
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
	 
	 @Bean
	 public UserDetailsService userDetailsService() {
		 System.out.println("UserDetails Service");
		 return new OurUserDetailService();
	 }
	
	 @Bean
	 public AuthenticationProvider authenticationProvider() {
		 System.out.println("Authentication Provider");
		 DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		 daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		 return daoAuthenticationProvider;
	 }
	
	 @Bean
	 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		 System.out.println("Authentication Manager");
		 return config.getAuthenticationManager();
	 }
}
