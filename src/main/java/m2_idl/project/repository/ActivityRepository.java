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
public interface ActivityRepository extends JpaRepository<Activity, Long> {


    @Query("SELECT a from Activity a where a.title like concat('%',:title,'%')")
    List<Activity> findListByTitle(String title);


    List<Activity> findByTitle(String title);
    List<Activity> findByYear(int year);

    List<Activity> findBytitleAndYear(String title,int year);
}
