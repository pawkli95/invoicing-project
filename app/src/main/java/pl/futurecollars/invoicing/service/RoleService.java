package pl.futurecollars.invoicing.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.roles.RolesRepository;
import pl.futurecollars.invoicing.model.Role;

@EnableJpaRepositories(basePackages = "pl.futurecollars.invoicing.db.roles")
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolesRepository rolesRepository;

    public List<Role> getRoles() {
        return rolesRepository.findAll();
    }
}
