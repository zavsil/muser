package com.wong.muser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wong.muser.backend.authentication.PasswordTools;

@Entity
@Table(schema = "public", name = "sys_user")

public class User {

  @Id
  @SequenceGenerator(name = "sys_user_id_seq", sequenceName = "sys_user_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_user_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer Id;

  @Column(nullable = false, columnDefinition = "boolean default true")
  private Boolean active;

  @Column(length = 20, name = "user_name")
  private String userName;

  @Column(name = "user_password", columnDefinition = "varchar")
  private String userPass;

  @Column(columnDefinition = "boolean default true")
  private Boolean changePassword;

  @ManyToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "role_id")
  private SysRole role;

  public User() {
    super();
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public String getUserPass() {
    return userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = PasswordTools.generateSecurePassword(userPass);

  }

  public SysRole getRole() {
    return role;
  }

  public void setRole(SysRole role) {
    this.role = role;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Boolean getChangePassword() {
    return changePassword;
  }

  public void setChangePassword(Boolean changePassword) {
    this.changePassword = changePassword;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  @Override
  public String toString() {
    return "User [Id=" + Id + ", role=" + role + ", active=" + active + ", userName=" + userName + ", userPass="
        + userPass + ", changePassword=" + changePassword + "]";
  }

}
