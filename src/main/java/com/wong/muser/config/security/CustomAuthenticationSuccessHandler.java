package com.wong.muser.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    // set our response to OK status
    response.setStatus(HttpServletResponse.SC_OK);

    for (GrantedAuthority auth : authentication.getAuthorities()) {
      if ("USER".equals(auth.getAuthority())) {
        response.sendRedirect("/dashboard");
      }
    }
  }

}
