package com.wong.muser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.Company;
import com.wong.muser.entity.CompanyDepartment;
import com.wong.muser.entity.Department;

@Repository
public interface CompanyDepartmentRepository extends JpaRepository<CompanyDepartment, Integer> {

  @Query(" select a.department from CompanyDepartment a where a.company = :comp")
  List<Department> findByCompanyOrderByDepartmentNameAsc(@Param("comp") Company company);

  // List<CompanyDepartment> findByCompanyOrderByDepartmentNameAsc(Company
  // company);

}
