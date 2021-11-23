package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import pl.futurecollars.invoicing.model.Role;

@Data
public class AuthResponse {

    @ApiModelProperty(value = "JWT")
    private final String token;

    @ApiModelProperty(value = "Role")
    private final Role role;
}
