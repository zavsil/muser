package com.wong.muser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.Company;
import com.wong.muser.entity.SysRole;
import com.wong.muser.entity.SysUserCompany;
import com.wong.muser.entity.User;

@Repository
public interface SysUserCompanyRepository extends JpaRepository<SysUserCompany, Integer> {

  List<SysUserCompany> findByCompanyAndUser(Company company, User user);

  List<SysUserCompany> findAllByCompany(Company company);

  List<SysUserCompany> findByUser(User user);

  SysUserCompany findByUserUserNameAndUserUserPassAndUserActiveAndCompany(String username, String password,
      boolean active, Company company);

  List<SysUserCompany> findAllByCompanyAndUserRole(Company company, SysRole role);

  List<SysUserCompany> findAllByCompanyAndUserActiveOrderByUserUserNameAsc(Company company, boolean active);

  @Query("select a from SysUserCompany a where  company = :dcompany and a.user.role.code=:dcode")
  List<SysUserCompany> findUsersByCompanyAndUserRoleCode(@Param("dcompany") Company company,
      @Param("dcode") String code);

}
