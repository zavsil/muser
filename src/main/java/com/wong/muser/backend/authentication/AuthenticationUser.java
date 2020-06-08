package com.wong.muser.backend.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wong.muser.entity.User;

public class AuthenticationUser implements UserDetails {

  private static final long serialVersionUID = 3104117137228243302L;

  private User user;
  private String schema;

  public AuthenticationUser(User user) {
    this(user, null);
  }

  public AuthenticationUser(User user, String schema) {
    this.user = user;
    this.schema = schema != null ? schema : "demo";
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>(); // getUserAuthority(user.getUserRole());
    authorities.add(new SimpleGrantedAuthority("USER"));

    return authorities;
  }

  public Integer getId() {
    return user.getId();
  }

  public String getSchema() {
    return schema;
  }

  @Override
  public String getPassword() {
    return user.getUserPass();
  }

  @Override
  public String getUsername() {
    return user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getActive();
  }

}
