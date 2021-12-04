package m2_idl.project.service;

import lombok.RequiredArgsConstructor;
import m2_idl.project.model.Activity;
import m2_idl.project.model.Nature;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.ActivityRepository;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.web.ActivityController;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopulateService {


    private final PasswordEncoder passwordEncoder;
    @Autowired
    private XUserRepository repo;
    @Autowired
    private XUserService service;

    @Autowired
    private ActivityRepository activityRepository;

    @PostConstruct
    public void populate(){
        String password = passwordEncoder.encode("pass");
        if (repo.count() == 0) {
            for (int i = 0; i < 1000; i++) {
                XUser xUser = new XUser();
                xUser.setEmail("User" + i + "@gmail.com");
                xUser.setPassword(password);
                xUser.setFirstname("firstname" + i);
                xUser.setLastname("lastname" + i);
                xUser.setBirthday(new Date(1999, 3, 5));
                xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
                xUser.setWebsite("https://hello" + i + ".com");
                xUser.setToken(null);

                repo.save(xUser);

                Activity a1 = new Activity("testxp" + i, 1999,
                        Nature.PROFESSIONAL_EXPERIENCES, "desc" + i, "https://blabla" + i + ".com",xUser);


                activityRepository.save(a1);


            }
        }
    }
}
