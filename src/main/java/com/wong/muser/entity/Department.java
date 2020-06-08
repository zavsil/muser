package com.wong.muser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class Department {
  @Id
  @SequenceGenerator(name = "department_id_seq", sequenceName = "department_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer Id;

  @Column(length = 60)
  private String name;

  private Boolean active;

  public Department() {
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

  @Override
  public String toString() {
    return "Department [Id=" + Id + ", name=" + name + ", active=" + active + "]";
  }

}
