package m2_idl.project.service;

import lombok.RequiredArgsConstructor;
import m2_idl.project.model.Activity;
import m2_idl.project.model.Nature;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.ActivityRepository;
import m2_idl.project.repository.XUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopulateService {


    private final PasswordEncoder passwordEncoder;
    public static final int userNumber = 100000;
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
            for (int i = 0; i < userNumber; i++) {

                XUser xUser = new XUser();
                if(i == 0 ){
                    xUser.setEmail("test@test.com");
                    xUser.setPassword(passwordEncoder.encode("test"));
                    xUser.setFirstname("testFN");
                    xUser.setLastname("testLN");
                    xUser.setBirthday("2014-02-14");
                    xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
                    xUser.setWebsite("https://test.com");
                    xUser.setToken(null);

                    repo.save(xUser);

                    Activity a1 = new Activity("titretest" + i, 1999,
                            Nature.PROFESSIONAL_EXPERIENCES, "desc" + i, "https://blabla" + i + ".com",xUser);

                    activityRepository.save(a1);
                }else {
                    xUser.setEmail("user" + i + "@gmail.com");
                    xUser.setPassword(password);
                    xUser.setFirstname("firstname" + i);
                    xUser.setLastname("lastname" + i);
                    xUser.setBirthday("2014-02-14");
                    xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
                    xUser.setWebsite("https://hello" + i + ".com");
                    xUser.setToken(null);

                    repo.save(xUser);

                    Activity a1 = new Activity("titre" + i, 1999,
                            Nature.PROFESSIONAL_EXPERIENCES, "desc" + i, "https://blabla" + i + ".com", xUser);

                    activityRepository.save(a1);
                }
            }
        }
    }


}
