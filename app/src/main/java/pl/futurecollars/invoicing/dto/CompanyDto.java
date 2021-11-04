package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

    @ApiModelProperty(value = "Id of company", example = "f77de595-58a1-4533-b96b-e493aee29e8a")
    private UUID id;

    @Size(min = 10, max = 10)
    @ApiModelProperty(value = "Tax identification number of company", required = true, example = "1002020100")
    private String taxIdentificationNumber;

    @NotBlank
    @ApiModelProperty(value = "Name of company", required = true, example = "PepsiCo")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "Address of company", required = true, example = "ul. Krótka 22, Warszawa 04-988")
    private String address;

    @NotNull
    @ApiModelProperty(value = "Health insurance", required = true, example = "4250.9")
    private BigDecimal healthInsurance;

    @NotNull
    @ApiModelProperty(value = "Pension insurance", required = true, example = "900.3")
    private BigDecimal pensionInsurance;
}
