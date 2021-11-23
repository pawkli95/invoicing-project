package pl.futurecollars.invoicing.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.futurecollars.invoicing.dto.UserDto;
import pl.futurecollars.invoicing.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    // TODO: MP : chyba nieuzywana metoda
    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}
