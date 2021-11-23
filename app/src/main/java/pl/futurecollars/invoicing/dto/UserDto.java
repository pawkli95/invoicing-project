package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.futurecollars.invoicing.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @ApiModelProperty(value = "Id", example = "f77de595-58a1-4533-b96b-e493aee29e8a")
    private UUID id;

    @ApiModelProperty(value = "Username/email", example = "p@wp.pl")
    @NotNull
    @Email
    private String username;

    @ApiModelProperty(value = "Password", example = "Password2")
    @NotNull
    private String password;

    @ApiModelProperty(value = "First name", example = "Paweł")
    @NotNull
    private String firstName;

    @ApiModelProperty(value = "Last name", example = "Klimek")
    @NotNull
    private String lastName;

    @ApiModelProperty(value = "Registration date", example = "2020-12-21")
    private LocalDate registrationDate;

    @ApiModelProperty(value = "Role")
    private Role role;
}
