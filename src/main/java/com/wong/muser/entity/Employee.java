package com.wong.muser.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "employee")

public class Employee {

  @Id
  @SequenceGenerator(name = "employee_id_seq", sequenceName = "employee_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer Id;

  @Column(length = 30, name = "first_name")
  private String firstName;

  @Column(length = 30, name = "last_name")
  private String lastName;

  @Column(length = 30, name = "phone")
  private String phone;

  @Column(name = "initial_date")
  private LocalDate initialDate;

  @Column(name = "final_date")
  private LocalDate finalDate;

  private String email;

  private Boolean active;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "company_id")
  private Company company;

  public Employee() {
    super();
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocalDate getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(LocalDate initialDate) {
    this.initialDate = initialDate;
  }

  public LocalDate getFinalDate() {
    return finalDate;
  }

  public void setFinalDate(LocalDate finalDate) {
    this.finalDate = finalDate;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  @Override
  public String toString() {
    return "Employee [Id=" + Id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
  }

}
