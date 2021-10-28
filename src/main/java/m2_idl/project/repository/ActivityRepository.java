package m2_idl.project.repository;


import m2_idl.project.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ActivityRepository extends JpaRepository<Activity, Long> {


}
