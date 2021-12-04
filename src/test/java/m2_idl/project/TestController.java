package m2_idl.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.service.PopulateService;
import m2_idl.project.service.XUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TestController {

    @Autowired
    MockMvc mvc;
    @Autowired
    XUserRepository repo;
    @Autowired
    XUserService service;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void assertGetUser(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8081/users";
        ResponseEntity< XUser[]> response
                = restTemplate.getForEntity(fooResourceUrl,  XUser[].class);


        XUser[] xUsers =response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assert xUsers != null;
        assertEquals(PopulateService.userNumber,xUsers.length);



    }


    @Test
    public void assertId(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8081/users/1";
        ResponseEntity<XUser> response
                = restTemplate.getForEntity(fooResourceUrl, XUser.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        XUser test = response.getBody();

        assert test != null;
        assertNotNull(test.getBirthday());


        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            String url
                    = "http://localhost:8081/users/100000000";
            ResponseEntity<XUser> responseEntity
                    = restTemplate.getForEntity(url, XUser.class);
        });


    }

    @Test
    public void assertDelete(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8081/users/2";
        ResponseEntity<String> response
                = restTemplate.exchange(fooResourceUrl, HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()),String.class);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }




    @Test
    public void assertPostUsers() throws Exception {



        XUser xUser = new XUser();
        xUser.setId(3545L);
        xUser.setEmail("philipe@gmail.com");
        xUser.setPassword(service.getEncodedPass("pass"));
        xUser.setFirstname("firstdname" + 99);
        xUser.setLastname("lastnadme" + 99);
        xUser.setBirthday("2000-04-03");
        xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
        xUser.setWebsite("https://helldo" + 99 + ".com");
        xUser.setToken("aaaaa");



        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(xUser)))
                .andExpect(status().isOk());



    }

    @Test
    public void assertPut() throws Exception {

        service.signin("User1@gmail.com","pass");
        XUser xUser1 = repo.findByEmail("User1@gmail.com");

        xUser1.setFirstname("aaaaaaaaaaaaaaaaaaa");


        mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,xUser1.getToken())
                .content(objectMapper.writeValueAsString(xUser1)))
                .andExpect(status().isOk());

    }


}
