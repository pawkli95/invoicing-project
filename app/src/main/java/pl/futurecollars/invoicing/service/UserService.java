package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.users.UserRepository;
import pl.futurecollars.invoicing.dto.UserDto;
import pl.futurecollars.invoicing.dto.mappers.UserMapper;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.model.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConstraintException("Username already in use");
        }
        return userRepository.save(user);
    }

    public UserDto getUser(UUID id) {
        User user = userRepository.getById(id);
        return userMapper.toDto(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
