package m2_idl.project.web;


import m2_idl.project.dto.ActivityDTO;
import m2_idl.project.dto.XUserDTO;
import m2_idl.project.model.Activity;
import m2_idl.project.model.Nature;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.ActivityRepository;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.service.XUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    XUserRepository repo;

    @Autowired
    ActivityRepository activity;

    @GetMapping()
    public Iterable<Activity> getActivity() {
        return  activity.findAll();
    }

    @GetMapping("/{id}")
    public Activity getActivityById(@PathVariable Long id){
        return activity.findById(id).get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteActivity(@PathVariable Long id) {
/*        for(int i = 0 ; i < repo.getById(id).getCv().size() ; i ++){
            System.out.println("aaa");
            if( repo.getById(id).getCv().get(i).getId().equals(id)){
                repo.getById(id).getCv().remove(i);
            }
        }
        for(int i = 0 ; i < repo.getById(id).getCv().size() ; i ++){
            System.out.println(repo.getById(id).getCv());
        }
        */
        activity.deleteById(id);
    }


    @PostMapping()
    Activity createActivity(@RequestBody ActivityDTO activityDTO){
        ModelMapper mapper  = new ModelMapper();
        Activity activity = mapper.map(activityDTO,Activity.class);
        this.activity.save(activity);
        return activity;

    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> putUser(@PathVariable Long id,@Valid @RequestBody Activity a ) throws Exception {
        Activity activ = activity.findById(id)
                .orElseThrow(() -> new Exception("User not found for this id : " + id));

        activ.setTitle(a.getTitle());
        activ.setYear(a.getYear());
        activ.setNature(a.getNature());
        activ.setDesc(a.getDesc());
        activ.setWebsite(a.getWebsite());





        final Activity updatedAct = activity.save(activ);
        return ResponseEntity.ok(updatedAct);
    }
}
