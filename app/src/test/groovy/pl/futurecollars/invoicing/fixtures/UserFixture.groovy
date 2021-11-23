package pl.futurecollars.invoicing.fixtures

import org.mapstruct.factory.Mappers
import pl.futurecollars.invoicing.dto.UserDto
import pl.futurecollars.invoicing.dto.mappers.UserMapper
import pl.futurecollars.invoicing.model.Role
import pl.futurecollars.invoicing.model.User

import java.time.LocalDate

class UserFixture {

    static UserMapper userMapper = Mappers.getMapper(UserMapper.class)

    public static UserDto getUserDto() {
        return UserDto
                .builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .id(UUID.randomUUID())
                .username("ak@wp.pl")
                .password("pass")
                .role(new Role("USER"))
                .registrationDate(LocalDate.now())
                .build()
    }

    static User getUser() {
        return userMapper.toEntity(getUserDto())
    }
}
