package pl.futurecollars.invoicing.db.users;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.futurecollars.invoicing.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByUsername(String username);
}
