package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.service.CompanyService;

@CrossOrigin
@Api(tags = {"company-controller"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @ApiOperation(value = "Add new company")
    @PostMapping
    public ResponseEntity<CompanyDto> save(@RequestBody @Valid CompanyDto companyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.saveCompany(companyDto));
    }

    @ApiOperation(value = "Get list of all companies")
    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAll());
    }

    @ApiOperation(value = "Get company by id")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getById(id));
    }

    @ApiOperation(value = "Update company")
    @PutMapping
    public ResponseEntity<CompanyDto> update(@RequestBody @Valid CompanyDto companyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.update(companyDto));
    }

    @ApiOperation(value = "Delete company by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
