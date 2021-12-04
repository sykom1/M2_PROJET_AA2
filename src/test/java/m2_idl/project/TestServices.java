package m2_idl.project;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.service.XUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TestServices {

    @Autowired
    XUserService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    XUserRepository repo;

    @Test
    public void assertSave() {
        XUser xUser = new XUser();
        xUser.setEmail("sah@gmail.com");
        xUser.setPassword("pass");
        xUser.setFirstname("firstname" + 99);
        xUser.setLastname("lastname" + 99);
        xUser.setBirthday("2000-04-03");
        xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
        xUser.setWebsite("https://hello" + 99 + ".com");
        xUser.setToken(null);


        repo.save(xUser);
        assertNotNull(repo.findByEmail("sah@gmail.com"));
        assertEquals("firstname99", repo.findByEmail("sah@gmail.com").getFirstname());
    }

    @Test
    public void assertJWT() throws InterruptedException {
        // Maintenant et cinq secondes plus tard
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.SECOND, 5);
        Date nowPlus5Seconds = c.getTime();

        // un secret pour signer le token
        String secretText = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        byte[] secret = TextCodec.BASE64.decode(secretText);

        // construction d'un JWT
        String jws = Jwts.builder()//
                .setIssuer("Jean-Luc MASSAT")//
                .setSubject("Test JWT")//
                .claim("name", "JLM")//
                .claim("scope", "admin")//
                .setIssuedAt(now)//
                .setExpiration(nowPlus5Seconds)//
                .signWith(SignatureAlgorithm.HS256, secret).compact();


        TimeUnit.SECONDS.sleep(5);
        // Décodage d'un JWT
        assertThrows(ExpiredJwtException.class, () -> {
            Jws<Claims> jwsDecoded = Jwts.parser()//
                    .setSigningKey(secret)//
                    .parseClaimsJws(jws);
            System.out.println(jwsDecoded);
        });
    }


    @Test
    @Transactional
    public void shouldGenerateAuthToken() throws Exception {


        XUser xUser = new XUser();
        xUser.setEmail("philipe@gmail.com");
        xUser.setPassword(service.getEncodedPass("pass"));
        xUser.setFirstname("firstdname" + 99);
        xUser.setLastname("lastnadme" + 99);
        xUser.setBirthday("2000-04-03");
        xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
        xUser.setWebsite("https://helldo" + 99 + ".com");
        xUser.setToken(null);
        repo.save(xUser);
        String token = service.signin(xUser.getEmail(),"pass");
        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/").header("Authorization", token)).andExpect(status().isOk());
    }

    @Test
    public void assertDelete(){
        assertNotNull(repo.findByEmail("User1@gmail.com"));
        service.delete("User1@gmail.com");
        assertNull(repo.findByEmail("User1@gmail.com"));
    }

    @Test
    @Transactional
    public void assertSetToken(){
        XUser test = repo.findByEmail("User1@gmail.com");
        assertNull(test.getToken());
        service.saveToken(test.getEmail(),"pass");
        assertNotNull(test.getToken());
    }


    @Test
    public void assertSearch(){

        XUser test = service.search("User1@gmail.com");
        assertEquals("firstname1",test.getFirstname());
    }


    @Test
    @Transactional
    public void asserLogout(){
        service.signin("User1@gmail.com","pass");
        assertNotNull(repo.findByEmail("User1@gmail.com").getToken());
        service.logout(repo.findByEmail("User1@gmail.com").getToken());
        assertNull(repo.findByEmail("User1@gmail.com").getToken());



    }

}
