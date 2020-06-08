package com.wong.muser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "application")

public class Application {

  @Id
  @SequenceGenerator(name = "application_id_seq", sequenceName = "application_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "url")
  private String url;

  @Column(nullable = false, columnDefinition = "boolean default true")
  private Boolean active;

  public Application() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    id = id;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Application [Id=" + id + ", name=" + name + ", active=" + active + "]";
  }

}
