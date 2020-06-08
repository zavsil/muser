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

@Entity
@Table(schema = "public", name = "sys_user_app")

public class SysUserApp {

  @Id
  @SequenceGenerator(name = "sys_user_app_id_seq", sequenceName = "sys_user_app_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_user_app_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer Id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "application_id")
  private Application application;

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public String toString() {
    return "SysUserApp [Id=" + Id + ", user=" + user + ", application=" + application + "]";
  }

}
