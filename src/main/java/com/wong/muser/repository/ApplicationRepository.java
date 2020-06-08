package com.wong.muser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

  List<Application> findDistinctByIdNotIn(List<Integer> ids);

}
