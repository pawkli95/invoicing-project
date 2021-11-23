package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthParams {

    @ApiModelProperty(value = "Username/email", example = "p@wp.pl")
    @NotNull
    @Email
    private String username;

    @ApiModelProperty(value = "Password", example = "Password2")
    @NotNull
    private String password;
}
