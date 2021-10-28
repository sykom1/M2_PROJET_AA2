package m2_idl.project.repository;

import m2_idl.project.model.XUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface XUserRepository extends JpaRepository<XUser, Long> {

     XUser findByEmail(String email);

     boolean existsByEmail(String email);


     @Transactional
     void deleteByEmail(String email);

     @Query("SELECT u from XUser u where u.token = ?1")
     XUser findByToken(String token);

}
