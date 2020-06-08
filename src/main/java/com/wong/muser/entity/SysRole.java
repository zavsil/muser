package com.wong.muser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sys_role")
public class SysRole {

  @Id
  @SequenceGenerator(name = "sys_role_id_seq", sequenceName = "sys_role_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_role_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer Id;

  @Column(length = 30)
  private String name;

  @Column(nullable = false, columnDefinition = "boolean default true")
  private Boolean active;

  @Column(length = 10)
  private String code;

  public SysRole() {
    super();
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "SysRole [Id=" + Id + ", name=" + name + ", active=" + active + ", code=" + code + "]";
  }

}
