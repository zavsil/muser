package com.wong.muser.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

  List<Company> findByOrderByNameAsc(Pageable pageWithElements);

  List<Company> findDistinctByIdNotIn(List<Integer> ids);

  List<Company> findByOrderByDisplayOrderAsc();

}
