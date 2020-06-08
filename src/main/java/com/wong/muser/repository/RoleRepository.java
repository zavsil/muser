package com.wong.muser.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.SysRole;

@Repository
public interface RoleRepository extends JpaRepository<SysRole, Integer> {

  List<SysRole> findByOrderByNameAsc(Pageable pageWithElements);

  SysRole findByCode(String code);

}
