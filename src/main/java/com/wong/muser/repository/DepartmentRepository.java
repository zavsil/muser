package com.wong.muser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {


}
