package pl.futurecollars.invoicing.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private UUID id;

    private String authority;
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    @Override
    public String getAuthority() {
        return null;
    }
}
