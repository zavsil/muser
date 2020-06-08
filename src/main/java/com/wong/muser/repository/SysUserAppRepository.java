package com.wong.muser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.SysUserApp;
import com.wong.muser.entity.User;

@Repository
public interface SysUserAppRepository extends JpaRepository<SysUserApp, Integer> {

  List<SysUserApp> findByUser(User user);

}
