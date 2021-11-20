package pl.futurecollars.invoicing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.model.User;
import pl.futurecollars.invoicing.db.users.UserRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException("Username already exists");
        }
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
