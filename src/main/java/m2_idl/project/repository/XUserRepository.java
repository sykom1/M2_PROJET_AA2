package m2_idl.project.repository;

import m2_idl.project.model.Activity;
import m2_idl.project.model.XUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface XUserRepository extends JpaRepository<XUser, Long> {

     XUser findByEmail(String email);
     List<XUser> findListByEmail(String email);

     @Query("SELECT u from XUser u where u.firstname like concat('%',:firstname,'%')")
     List<XUser> findListByFirstname(String firstname);
     @Query("SELECT u from XUser u where u.lastname like concat('%',:lastname,'%')")
     List<XUser> findListByLastname(String lastname);


     @Query("SELECT distinct u " +
             "from XUser u, Activity a " +
             "where (SELECT a from Activity a where a.title like concat('%',:title,'%'))" +
             " member u.cv ")
     List<XUser> findListByTitle(String title);

//     and a.title like concat('%',:title,'%')
     boolean existsByEmail(String email);


     @Transactional
     void deleteByEmail(String email);



     @Query("SELECT u from XUser u where u.token = ?1")
     XUser findByToken(String token);

}
