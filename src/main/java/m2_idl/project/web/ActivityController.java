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
import org.hibernate.annotations.GeneratorType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    XUserRepository repo;

    @Autowired
    ActivityRepository activity;

    @GetMapping()
    public Iterable<Activity> getActivity(@PathParam("title") String title, @PathParam("year") Integer year) {

        if(title == null && year==null){
            return  activity.findAll();
        }else if(title != null && year == null){
            return activity.findByTitle(title);

        }else if(title == null){
            return activity.findByYear(year);
        }
        else{
            Activity a = activity.findBytitleAndYear(title,year).get(0);
            ModelMapper modelMapper = new ModelMapper();
            ActivityDTO  movieDTO = modelMapper.map(a, ActivityDTO.class);
            return activity.findBytitleAndYear(title,year);
        }

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


        System.out.println(activity.getById(id));
        System.out.println(activity.getById(id).getUser());
        System.out.println(activity.getById(id).getUser().getCv());
        activity.getById(id).getUser().getCv().remove(activity.getById(id));

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
    public ResponseEntity<Activity> putActivity(@PathVariable Long id,@Valid @RequestBody ActivityDTO activityDTO ) throws Exception {
        Activity activ = activity.findById(id)
                .orElseThrow(() -> new Exception("Activity not found for this id : " + id));
        ModelMapper mapper  = new ModelMapper();
        Activity a = mapper.map(activityDTO,Activity.class);

        activ.setTitle(a.getTitle());
        activ.setYear(a.getYear());
        activ.setNature(a.getNature());
        activ.setDesc(a.getDesc());
        activ.setWebsite(a.getWebsite());
        activ.setUser(a.getUser());


        final Activity updatedAct = activity.save(activ);
        return ResponseEntity.ok(updatedAct);
    }


    @GetMapping("/search")
    public Iterable<Activity> getResearch(@PathParam("title") String title){

        List<Activity> activityList = activity.findAll();
        for(Activity activity : activityList){
            if(activity.getTitle().contains(title)){
                return this.activity.findListByTitle(title);
            }
        }

        return null;
    }
}
