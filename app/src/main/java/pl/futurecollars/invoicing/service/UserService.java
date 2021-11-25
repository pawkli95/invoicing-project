package pl.futurecollars.invoicing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.dto.UserDto;
import pl.futurecollars.invoicing.dto.mappers.UserMapper;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.model.User;
import pl.futurecollars.invoicing.repository.RolesRepository;
import pl.futurecollars.invoicing.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConstraintException("Username already in use");
        }
        user.setRegistrationDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(rolesRepository.findByAuthority("USER"));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto getUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User doesn't exist");
        }
        return userMapper.toDto(userRepository.getById(id));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User doesn't exist");
        }
        userRepository.deleteById(id);
    }
}
