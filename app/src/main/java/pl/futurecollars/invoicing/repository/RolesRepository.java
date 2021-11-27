package pl.futurecollars.invoicing.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, UUID> {
}
