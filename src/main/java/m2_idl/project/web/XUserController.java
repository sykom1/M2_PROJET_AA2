package m2_idl.project.web;

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
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class XUserController {

    @Autowired
    XUserRepository repo;

    @Autowired
    ActivityRepository activity;

    @Autowired
    XUserService service;


    @PostConstruct
    void populate(){
        ArrayList<XUser> users = new ArrayList<>();
        if(repo.count() == 0){
            for(int i = 0;i<10 ;i++){
                XUser xUser = new XUser();
                xUser.setEmail("User"+i+"@gmail.com");
                xUser.setPassword("pass");
                xUser.setBirthday(new Date(1999,3,5));
                xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
                xUser.setWebsite("https://hello" + i +".com");
                Activity a1 = new Activity("testxp"+i,1999,
                        Nature.PROFESSIONAL_EXPERIENCES,"desc"+i,"https://blabla"+i+".com");
                Activity a2 = new Activity("projects"+i,2000,Nature.PROJECTS);
                xUser.setCv(new ArrayList<>(List.of(a1,a2)));

                activity.save(a1);
                activity.save(a2);
                users.add(xUser);
                service.signup(xUser,false);
            }
        repo.saveAll(users);
        }
    }

    @GetMapping()
    public Iterable<XUser> getUser() {
        return  repo.findAll();
    }


    @GetMapping("/{id}")
    public XUser getUserById(@PathVariable Long id){
        return repo.findById(id).get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id) {
        repo.deleteById(id);
    }


    @PostMapping
    XUser createUser(@RequestBody XUserDTO xUserDTO){
        ModelMapper mapper  = new ModelMapper();
        XUser xUser = mapper.map(xUserDTO,XUser.class);
        repo.save(xUser);
        return xUser;

    }

}
