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
@Table(schema = "public", name = "company_department")

public class CompanyDepartment {

  @Id
  @SequenceGenerator(name = "company_department_id_seq", sequenceName = "company_department_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_department_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer Id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "department_id")
  private Department department;

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  @Override
  public String toString() {
    return "CompanyDepartment [Id=" + Id + ", company=" + company + ", department=" + department + "]";
  }

}
