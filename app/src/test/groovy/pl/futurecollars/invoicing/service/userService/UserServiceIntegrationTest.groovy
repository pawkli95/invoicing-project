package pl.futurecollars.invoicing.service.userService

import org.junit.Ignore
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.users.UserRepository
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

    UserMapper userMapper = Mappers.getMapper(UserMapper.class)

    User user = userMapper.toEntity(userDto)

    def setup() {
        clearDatabase()
    }

    def "should register user"() {
        when:
        userService.saveUser(userDto)

        then:
        userRepository.findAll().size() == 1
    }

    def "should throw ConstraintException when username already exists"() {
        given:
        userService.saveUser(userDto)

        when:
        userService.saveUser(userDto)

        then:
        thrown(ConstraintException)
    }


//    def "should return userDto by id"() {
//        given:
//        User returnedUser = userRepository.save(user)
//        UserDto returnedUserDto = userMapper.toDto(returnedUser)
//
//        when:
//        def response = userService.getUser(returnedUserDto.getId())
//
//        then:
//        response == returnedUserDto
//    }

    def "should return list of userDtos"() {
        given:
        userRepository.save(user)

        when:
        def list = userService.getUsers()

        then:
        list.size() == 1
    }

    def "should delete user by id"() {
        given:
        User returnedUser = userRepository.save(user)

        when:
        userService.deleteUser(returnedUser.getId())

        then:
        userService.getUsers().size() == 0
    }



    def clearDatabase() {
        userRepository.deleteAll()
    }
}
