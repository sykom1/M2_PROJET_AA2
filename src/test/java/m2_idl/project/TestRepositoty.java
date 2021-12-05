package m2_idl.project;

import m2_idl.project.model.Activity;
import m2_idl.project.model.XUser;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.service.XUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TestRepositoty {

    @Autowired
    XUserService service;
    @Autowired
    XUserRepository repo;




    @Test
    public void assertFindByMail(){
        XUser xUser = repo.findByEmail("user1@gmail.com");
        assertEquals("firstname1",xUser.getFirstname());
    }

    @Test
    public void assertFindListByFirstname(){
        List<XUser> xUsers = repo.findListByFirstname("firstname1");
        for (XUser xUser : xUsers){
            assertTrue(xUser.getFirstname().contains("firstname1"));
        }

    }

    @Test
    public void assertFindListByLastname(){
        List<XUser> xUsers = repo.findListByLastname("lastname1");
        for (XUser xUser : xUsers){
            assertTrue(xUser.getLastname().contains("lastname1"));
        }

    }

    @Test
    public void assertFindListByTitle(){
        List<XUser> xUsers = repo.findListByTitle("titre1");
        boolean isInTitle = false;
        for (XUser xUser : xUsers){
            for (Activity activity : xUser.getCv()){
                if (activity.getTitle().equals("titre10")) {
                    isInTitle = true;
                    break;
                }
            }

        }
        assertTrue(isInTitle);


        List<XUser> xUsers2 = repo.findListByTitle("itre1");
        boolean isInTitleENd = false;
        for (XUser xUser : xUsers2){
            for (Activity activity : xUser.getCv()){
                if (activity.getTitle().contains("titre10")) {
                    isInTitleENd = true;
                    break;
                }
            }

        }
        assertTrue(isInTitleENd);
    }


    @Test
    public void assertExistsByEmail(){
        assertTrue(repo.existsByEmail("user100@gmail.com"));
    }

    @Test
    public void assertDeleteByEmail(){
        assertTrue(repo.existsByEmail("user100@gmail.com"));
        repo.deleteByEmail("user100@gmail.com");
        assertFalse(repo.existsByEmail("user100@gmail.com"));
    }


    @Test
    public void assertFindByToken(){
        service.signin("user1@gmail.com","pass");
        XUser xUser1 = repo.findByEmail("user1@gmail.com");
        String token = xUser1.getToken();
        XUser xUser2 = repo.findByToken(token);

        assertEquals(xUser1,xUser2);
    }
}
