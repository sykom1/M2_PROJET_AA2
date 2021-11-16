package m2_idl.project.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class XUserDTO {

    Long id;
    String email;
    String password;
    String firstname;
    String lastname;
    String website;
    Date birthday;
}
