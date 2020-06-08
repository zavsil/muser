package com.wong.muser.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wong.muser.backend.authentication.AuthenticationUser;
import com.wong.muser.entity.User;
import com.wong.muser.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository repository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails result = null;
    User user = repository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    if (user.getActive()) {
      result = new AuthenticationUser(user);
    } else {
      new UsernameNotFoundException("Username not found.");
    }

    return result;
  }
}
