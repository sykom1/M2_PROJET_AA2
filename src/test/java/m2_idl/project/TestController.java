package m2_idl.project;

import m2_idl.project.model.XUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestController {

    @Test
    public void assertGetUser(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8081/users";
        ResponseEntity< XUser[]> response
                = restTemplate.getForEntity(fooResourceUrl,  XUser[].class);


        XUser[] xUsers =response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(10,xUsers.length);



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

        assertNotNull(test.getBirthday());


        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            String url
                    = "http://localhost:8081/users/100000";
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


}
