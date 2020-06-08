package com.wong.muser.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wong.muser.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

 // User findByUserName(String userName);

  Optional<User> findByUserName(String username);

  @Query("select a from User a  order by userName asc")
  List<User> findAllOrderByUserNameAsc();

  List<User> findByOrderByUserNameAsc(Pageable pageWithElements);

  @Query("select a from User a where" + " a.userName  like %:filter%" + " or lower(a.role.name)  like %:filter%")
  List<User> findAllByFilter(@Param("filter") String filter);

  @Query(value = " select s.*" + " from sys_user s " + " left join (select * from user_result where quiz_id=:qid) u "
      + " on s.id=u.user_id  " + " inner join sys_user_company c " + " on s.id=c.user_id " + " inner join sys_role r "
      + " on r.id=s.sys_role_id " + " where u.user_id is null and c.company_id =:compid "
      + " and r.code not LIKE 'ADMIN'", nativeQuery = true)
  List<User> findAllPending(@Param("qid") Integer quizId, @Param("compid") Integer companyId);

}
