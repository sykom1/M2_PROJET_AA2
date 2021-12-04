package m2_idl.project;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.RequiredArgsConstructor;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.service.XUserService;
import m2_idl.project.web.XUserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        xUser.setBirthday(new Date(1999, 3, 5));
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
        //xUser.setPassword("pass");

        xUser.setPassword(service.getEncodedPass("pass"));

        xUser.setFirstname("firstdname" + 99);
        xUser.setLastname("lastnadme" + 99);
        xUser.setBirthday(new Date(2000, 3, 5));
        xUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
        xUser.setWebsite("https://helldo" + 99 + ".com");
        xUser.setToken(null);

        repo.save(xUser);
        // repo.save(xUser);
        //Thread.sleep(2000);
        String token = service.signin(xUser.getEmail(),"pass");


      //  String token = service.signin("Jean", "123");

        assertNotNull(token);
        //mvc.perform(MockMvcRequestBuilders.get("/test").header("Authorization", token)).andExpect(status().isOk());
    }

}
