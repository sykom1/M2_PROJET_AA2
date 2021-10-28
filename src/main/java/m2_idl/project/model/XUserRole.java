package m2_idl.project.model;

import org.springframework.security.core.GrantedAuthority;

public enum XUserRole implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER;

    public String getAuthority() {
        return name();
    }

}
