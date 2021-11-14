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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class XUserController {

    @Autowired
    XUserRepository repo;



    @Autowired
    XUserService service;


    @PostConstruct
    void populate(){
        if(repo.count() == 0){
            for(int i = 0;i<10 ;i++){
                XUser xUser = new XUser();
                xUser.setEmail("User"+i+"@gmail.com");
                xUser.setPassword("pass");
                xUser.setFirstname("firstname"+i);
                xUser.setLastname("lastname"+i);
                xUser.setBirthday(new Date(1999,3,5));
                xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
                xUser.setWebsite("https://hello" + i +".com");
                Activity a1 = new Activity("testxp"+i,1999,
                        Nature.PROFESSIONAL_EXPERIENCES,"desc"+i,"https://blabla"+i+".com");
                Activity a2 = new Activity("projects"+i,2000,Nature.PROJECTS);
                xUser.setCv(new ArrayList<>(List.of(a1,a2)));
                service.signup(xUser);
            }
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
    XUser postUser(@RequestBody XUser xUser){
        repo.save(xUser);
        return xUser;

    }

    @PutMapping("/{id}")
    public ResponseEntity<XUser> putUser(@PathVariable Long id, @Valid @RequestBody XUser u ) throws Exception {


        XUser user = repo.findById(id)
                .orElseThrow(() -> new Exception("User not found for this id : " + id));


        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        user.setFirstname(u.getFirstname());
        user.setLastname(u.getLastname());
        user.setWebsite(u.getWebsite());
        user.setBirthday(u.getBirthday());
        user.setCv(u.getCv());

        final XUser updatedUser = repo.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/signin")
    public ModelAndView log(){
        return new ModelAndView("login");
    }

    @PostMapping("/signin")
    public ModelAndView login(//
                              @RequestParam String email, //
                              @RequestParam String password) {
        return new ModelAndView("token","token",service.signin(email,password));
    }

    @GetMapping("/signup")
    public ModelAndView signup(){
        return new ModelAndView("signup");
    }

    @PostMapping("/signup")
    public String signup(@RequestBody XUser xUser,
                         @RequestParam String email, //
                         @RequestParam String password) {

        xUser.setEmail(email);
        xUser.setPassword(password);
        return service.signup(xUser);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public String refresh(HttpServletRequest req) {
        return service.refresh(req.getRemoteUser());
    }

    @GetMapping("/logout")
    public void logout(@RequestHeader(value = "Authorization") String authorize){
        authorize = authorize.substring(7);
        service.logout(authorize);
    }


}
