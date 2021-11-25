package pl.futurecollars.invoicing.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.repository.UserRepository
import pl.futurecollars.invoicing.dto.UserDto
import pl.futurecollars.invoicing.dto.mappers.UserMapper
import pl.futurecollars.invoicing.fixtures.UserFixture
import pl.futurecollars.invoicing.model.User
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.http.MediaType

@AutoConfigureJsonTesters
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    JacksonTester<UserDto> userDtoJacksonTester

    @Autowired
    JacksonTester<List<UserDto>> userDtoListJacksonTester

    @Autowired
    UserRepository userRepository

    @Autowired
    UserMapper userMapper

    UserDto userDto = UserFixture.getUserDto()


    def setup() {
        clearDatabase()
    }

    def "should add user to database"() {
        given:
        String jsonString = userDtoJacksonTester.write(userDto).getJson()

        when:
        def response = mockMvc
                .perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        UserDto responseUserDto = userDtoJacksonTester.parseObject(response)
        responseUserDto.getUsername() == responseUserDto.getUsername()
        responseUserDto.getFirstName() == responseUserDto.getFirstName()
    }

    @WithMockUser(authorities = "ADMIN")
    def "should get all users if user has role admin"() {
        given:
        addUser()

        when:
        def response = mockMvc
                .perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        def list = userDtoListJacksonTester.parseObject(response)
        list.size() == 1
        list.get(0).getUsername() == userDto.getUsername()
    }

    @WithMockUser(authorities = "USER")
    def "should return 403 Forbidden when getting users if user hasn't got admin role"() {
        expect:
        mockMvc.perform(get("/api/users"))
        .andExpect(status().isForbidden())

    }

    @WithMockUser(authorities = "ADMIN")
    def "should delete user if user has role admin"() {
        given:
        User user = addUser()

        when:
        mockMvc
                .perform(delete("/api/users/" + user.getId().toString()))
                .andExpect(status().isAccepted())

        then:
        getAll().size() == 0
    }

    @WithMockUser(authorities = "USER")
    def "should return 403 Forbidden when deleting user if user hasn't got admin role"() {
        expect:
        mockMvc.perform(delete("/api/users/" + UUID.randomUUID().toString()))
                .andExpect(status().isForbidden())
    }

    @WithMockUser(authorities = "USER")
    def "should get user by id"() {
        given:
        User user = addUser()

        when:
        def response = mockMvc
                .perform(get("/api/users/" + user.getId().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        UserDto responseUserDto = userDtoJacksonTester.parseObject(response)
        user.setRole(responseUserDto.getRole())
        userMapper.toDto(user) == responseUserDto
    }


    def clearDatabase() {
        userRepository.deleteAll()
    }

    def addUser() {
        return userRepository.save(userMapper.toEntity(userDto))
    }

    def getAll() {
        return userRepository.findAll()
    }
}
