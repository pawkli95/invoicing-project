package pl.futurecollars.invoicing.service.integrationTests

import org.junit.Ignore
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.repository.UserRepository
import pl.futurecollars.invoicing.dto.UserDto
import pl.futurecollars.invoicing.dto.mappers.UserMapper
import pl.futurecollars.invoicing.exceptions.ConstraintException
import pl.futurecollars.invoicing.fixtures.UserFixture
import pl.futurecollars.invoicing.model.User
import pl.futurecollars.invoicing.service.UserService
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest extends Specification {

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    UserDto userDto = UserFixture.getUserDto()

    @Autowired
    UserMapper userMapper

    def setup() {
        clearDatabase()
    }

    def "should register user"() {
        when:
        userService.save(userDto)

        then:
        userRepository.findAll().size() == 1
    }

    def "should get user by id"() {
        given:
        User user = userMapper.toEntity(userDto)
        User returnedUser = userRepository.save(user)

        when:
        UserDto response = userService.getById(returnedUser.getId())

        then:
        returnedUser.setRole(response.getRole())
        response == userMapper.toDto(returnedUser)
    }

    def "should throw ConstraintException when username already exists"() {
        given:
        userService.save(userDto)

        when:
        userService.save(userDto)

        then:
        thrown(ConstraintException)
    }

    def "should throw NoSuchElementException when getting by id nonexistent user"() {
        when:
        userService.getById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }

    def "should return list of userDtos"() {
        given:
        addUser()

        when:
        def list = userService.getAll()

        then:
        list.size() == 1
    }

    def "should delete user by id"() {
        given:
        User returnedUser = addUser()

        when:
        userService.delete(returnedUser.getId())

        then:
        userService.getAll().size() == 0
    }

    def "should throw NoSuchElementException when deleting by id nonexistent user"() {
        when:
        userService.delete(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }



    def clearDatabase() {
        userRepository.deleteAll()
    }

    def addUser() {
        User user = userMapper.toEntity(userDto)
        return userRepository.save(user)
    }
}
