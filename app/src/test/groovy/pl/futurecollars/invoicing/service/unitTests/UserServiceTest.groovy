package pl.futurecollars.invoicing.service.unitTests

import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import pl.futurecollars.invoicing.repository.RolesRepository
import pl.futurecollars.invoicing.repository.UserRepository
import pl.futurecollars.invoicing.dto.UserDto
import pl.futurecollars.invoicing.dto.mappers.UserMapper
import pl.futurecollars.invoicing.exceptions.ConstraintException
import pl.futurecollars.invoicing.fixtures.UserFixture
import pl.futurecollars.invoicing.model.Role
import pl.futurecollars.invoicing.model.User
import pl.futurecollars.invoicing.service.UserService
import spock.lang.Specification

class UserServiceTest extends Specification{

    UserRepository userRepository;

    UserService userService;

    UserMapper userMapper = Mappers.getMapper(UserMapper.class)

    RolesRepository rolesRepository;

    UserDto userDto = UserFixture.getUserDto()

    User user = userMapper.toEntity(userDto)

    PasswordEncoder passwordEncoder

    def setup() {
        userRepository = Mock()
        rolesRepository = Mock()
        passwordEncoder = Mock()
        userService = new UserService(userRepository, rolesRepository, userMapper, passwordEncoder)
    }

    def "calling saveUser() should map dto to entity and delegate to repository save()"() {
        given:
        Role userRole = new Role("USER")
        rolesRepository.findAll() >> List.of(userRole)
        passwordEncoder.encode(userDto.getPassword()) >> "pass"
        userDto.setRole(userRole)

        when:
        userService.save(userDto)

        then:
        1 * userRepository.save(userMapper.toEntity(userDto))
    }

    def "should throw ConstraintException when username is already in use"() {
        given:
        userRepository.existsByUsername(user.getUsername()) >> true

        when:
        userService.save(userDto)

        then:
        thrown(ConstraintException)
    }

    def "should throw NoSuchElementException when getting user by id if user doesn't exist"() {
        given:
        UUID id = UUID.randomUUID()
        userRepository.findById(id) >> Optional.ofNullable(null)

        when:
        userService.getById(id)

        then:
        thrown(NoSuchElementException)
    }

    def "should map entity to dto and return userDto by id"() {
        given:
        userRepository.findById(user.getId()) >> Optional.of(user)

        when:
        def userResponse = userService.getById(userDto.getId())

        then:
        userResponse == userDto
    }

    def "should return list of UserDto"() {
        given:
        userRepository.findAll() >> [user]

        when:
        def list = userService.getAll()

        then:
        list == [userDto]
    }

    def "should delete user"() {
        when:
        userRepository.existsById(userDto.getId()) >> true
        userService.delete(userDto.getId())

        then:
        userRepository.deleteById(user.getId())
    }

    def "should throw NoSuchElementException when deleting user by id if user doesn't exist"() {
        given:
        UUID id = UUID.randomUUID()
        userRepository.existsById(id) >> false

        when:
        userService.delete(id)

        then:
        thrown(NoSuchElementException)
    }
}
