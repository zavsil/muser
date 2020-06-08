package com.wong.muser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company {
  @Id
  @SequenceGenerator(name = "company_id_seq", sequenceName = "company_id_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_seq")
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(length = 60)
  private String name;

  @Column(name = "address_line1")
  private String addressLine1;

  @Column(name = "address_line2")
  private String addressLine2;

  @Column(name = "address_city")
  private String addressCity;

  @Column(name = "address_zipcode")
  private String addressZipcode;

  @Column(name = "phone_work")
  private String phoneWork;

  @Column(name = "phone_other")
  private String phoneOther;

  @Column(name = "tax_code1")
  private String taxCode1;

  @Column(name = "tax_code2")
  private String taxCode2;

  private String image;

  private Boolean active;

  @Column(name = "display_order")
  private Integer displayOrder;

  public Company() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  public String getAddressZipcode() {
    return addressZipcode;
  }

  public void setAddressZipcode(String addressZipcode) {
    this.addressZipcode = addressZipcode;
  }

  public String getPhoneWork() {
    return phoneWork;
  }

  public void setPhoneWork(String phoneWork) {
    this.phoneWork = phoneWork;
  }

  public String getPhoneOther() {
    return phoneOther;
  }

  public void setPhoneOther(String phoneOther) {
    this.phoneOther = phoneOther;
  }

  public String getTaxCode1() {
    return taxCode1;
  }

  public void setTaxCode1(String taxCode1) {
    this.taxCode1 = taxCode1;
  }

  public String getTaxCode2() {
    return taxCode2;
  }

  public void setTaxCode2(String taxCode2) {
    this.taxCode2 = taxCode2;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

}
