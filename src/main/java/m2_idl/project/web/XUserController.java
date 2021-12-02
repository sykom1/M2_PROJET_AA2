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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

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
    @Autowired
    ActivityRepository activityRepository;


    @PostConstruct
    void populate() {
        if (repo.count() == 0) {
            for (int i = 0; i < 10; i++) {

                XUser xUser = new XUser();
                xUser.setEmail("User" + i + "@gmail.com");
                xUser.setPassword("pass");
                xUser.setFirstname("firstname" + i);
                xUser.setLastname("lastname" + i);
                xUser.setBirthday(new Date(1999, 3, 5));
                xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
                xUser.setWebsite("https://hello" + i + ".com");
                xUser.setToken(null);




                service.signup(xUser);
                Activity a1 = new Activity("testxp" + i, 1999,
                        Nature.PROFESSIONAL_EXPERIENCES, "desc" + i, "https://blabla" + i + ".com",xUser);



                activityRepository.save(a1);


            }
        }
    }

    @GetMapping()
    public Iterable<XUser> getUser(@PathParam("email") String email) {

        if (email == null) {
            return repo.findAll();
        } else {
            List<XUser> listByEmail = repo.findListByEmail(email);

            ModelMapper modelMapper = new ModelMapper();
            XUserDTO xUserDTO = modelMapper.map(listByEmail, XUserDTO.class);
            return repo.findListByEmail(email);
        }
    }
    @RequestMapping(value = "/page/{id}", method = RequestMethod.GET)
    Iterable<XUser> getByPage(@PathVariable int id) {

        Page<XUser> pageData = repo.findAll(PageRequest.of(id, 10));

        return pageData.getContent();

    }




    @GetMapping("/{id}")
    public XUser getUserById(@PathVariable Long id) {
        return repo.findById(id).get();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id) {
        repo.deleteById(id);
    }


    @PostMapping
    XUser postUser(@RequestBody XUserDTO xUserDTO) {
        ModelMapper mapper = new ModelMapper();
        XUser xUser = mapper.map(xUserDTO, XUser.class);
        repo.save(xUser);
        return xUser;

    }

    @PutMapping("/{id}")
    public ResponseEntity<XUser> putUser(@PathVariable Long id, @Valid @RequestBody XUser u, @RequestHeader(value = "Authorization") String authorize) throws Exception {
        String token = authorize.substring(7);

        XUser user = repo.findById(id)
                .orElseThrow(() -> new Exception("User not found for this id : " + id));


        if(repo.findByToken(token) != null){
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
        return null;

    }

    @GetMapping("/signin")
    public ModelAndView log() {
        return new ModelAndView("login");
    }

    @PostMapping("/signin")
    public ModelAndView login(//
                              @RequestParam String email, //
                              @RequestParam String password) {
        return new ModelAndView("token", "token", service.signin(email, password));
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        return new ModelAndView("signup");
    }

    @PostMapping("/signup")
    public void signup(@RequestBody XUser xUser,
                       @RequestParam String email, //
                       @RequestParam String password,@RequestHeader(value = "Authorization") String authorize) {
        String token = authorize.substring(7);
        if(repo.findByToken(token) != null){
            xUser.setEmail(email);
            xUser.setPassword(password);
            service.signup(xUser);
        }

    }


    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public String refresh(HttpServletRequest req) {
        return service.refresh(req.getRemoteUser());
    }

    @GetMapping("/logout")
    public ModelAndView logout(@RequestHeader(value = "Authorization") String authorize) {
        String token = authorize.substring(7);
        service.logout(token);
        return new ModelAndView("token", "token", "");

    }

    @GetMapping("/search")
    public Iterable<XUser> getResearch(@PathParam("name") String name) {
        if (name != null) {


            List<XUser> xUserList = repo.findAll();
            for (XUser xUser : xUserList) {

                if (xUser.getFirstname().contains(name)) {
                    return repo.findListByFirstname(name);
                } else if (xUser.getLastname().contains(name)) {
                    return repo.findListByLastname(name);
                }
                else {
                    for(Activity activity : xUser.getCv()){

                        if(activity.getTitle().contains(name)){
                            System.out.println(activity.getTitle() +" "+name);
                            return repo.findListByTitle(name);
                        }
                    }

                }

            }

        } else {
            return repo.findAll();
        }
        return null;
    }
}
