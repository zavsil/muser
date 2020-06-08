package com.wong.muser.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wong.muser.backend.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/login";
  private static final String LOGIN_FAILURE_URL = "/login?error";
  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = "/login";
  
  @Autowired
  private CustomUserDetailsService userDetailsService;
  @Autowired
  private CustomAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
  
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      // Register our CustomRequestCache that saves unauthorized access attempts, so
      // the user is redirected after login.
      .requestCache().requestCache(new CustomRequestCache())
      // Restrict access to our application.
      .and()
      .authorizeRequests()
      // Allow all flow internal requests.
      .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
      // Allow login with parameters
      .antMatchers("/login/*").permitAll()
      // Allow all requests by logged in users.
      .anyRequest().authenticated()
      // Configure the login page.
      .and()
      .formLogin()
      .loginPage(LOGIN_URL).permitAll()
      .loginProcessingUrl(LOGIN_PROCESSING_URL)
      .failureUrl(LOGIN_FAILURE_URL)
      .successHandler(customizeAuthenticationSuccessHandler)
      .and()
      // Configure logout
      .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID")
      .logoutSuccessUrl(LOGOUT_SUCCESS_URL);
      
//      .and().httpBasic()
//      // STATELESS is causing SecurityContextHolder.getContext().getAuthentication() always be null.
//      // https://github.com/szerhusenBC/jwt-spring-security-demo/issues/33
//      // .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//      .and().headers().frameOptions().disable();
  }

  /**
   * Allows access to static resources, bypassing Spring security.
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        // Client-side JS
        "/VAADIN/**",
        // the standard favicon URI
        "/favicon.ico",
        // the robots exclusion standard
        "/robots.txt",
        // web application manifest
        "/manifest.webmanifest", "/sw.js", "/offline.html",
        // (development mode) static resources (icons, images, styles, ...)
        "/frontend/**",
        // (development mode) H2 debugging console
        "/h2-console/**",
        // (development mode) webjars // 
        "/webjars/**",
        // (production mode) static resources // 
        "/frontend-es5/**", "/frontend-es6/**");
  }
}
