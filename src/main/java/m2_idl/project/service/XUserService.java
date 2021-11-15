package m2_idl.project.service;



import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import m2_idl.project.exception.CustomException;
import m2_idl.project.model.Activity;
import m2_idl.project.model.XUser;
import m2_idl.project.model.XUserRole;
import m2_idl.project.repository.ActivityRepository;
import m2_idl.project.repository.XUserRepository;
import m2_idl.project.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class XUserService {

    private final XUserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    public String signin(String UserName, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(UserName, password));
            String token = jwtTokenProvider.createToken(UserName, userRepository.findByEmail(UserName).getRoles());
            userRepository.findByEmail(UserName).setToken(token);
            userRepository.save(userRepository.findByEmail(UserName));

            return token;
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid UserName/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void signup(XUser XUser) {
        if (!userRepository.existsByEmail(XUser.getEmail())) {
            XUser.setPassword(passwordEncoder.encode(XUser.getPassword()));
            XUser.setRoles(new ArrayList<>(List.of(XUserRole.ROLE_USER)));
            for(Activity activity : XUser.getCv()){
                activityRepository.save(activity);
            }
            userRepository.save(XUser);
        } else {
            throw new CustomException("UserName is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    public void delete(String UserName) {
        userRepository.deleteByEmail(UserName);
    }
    public void saveToken(String username,String password){
        userRepository.findByEmail(username).setToken(signin(username,password));
    }

    public XUser search(String UserName) {
        XUser XUser = userRepository.findByEmail(UserName);
        if (XUser == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return XUser;
    }

    public XUser whoami(HttpServletRequest req) {
        return userRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String UserName) {
        return jwtTokenProvider.createToken(UserName, userRepository.findByEmail(UserName).getRoles());
    }

    public void logout(String token){

        XUser XUser = userRepository.findByToken(token);
        jwtTokenProvider.invalidToker(XUser.getToken());
        XUser.setToken(null);
        userRepository.save(XUser);
    }

}
