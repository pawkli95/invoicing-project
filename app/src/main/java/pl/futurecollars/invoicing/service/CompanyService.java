package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper;
import pl.futurecollars.invoicing.model.Company;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Database<Company> companyDatabase;

    private final CompanyMapper companyMapper;

    public CompanyDto saveCompany(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        Company returnedCompany = companyDatabase.save(company);
        return companyMapper.toDto(returnedCompany);
    }

    public CompanyDto getById(UUID id) {
        Company company = companyDatabase.getById(id);
        return companyMapper.toDto(company);
    }

    public void delete(UUID id) throws NoSuchElementException {
        companyDatabase.delete(id);
    }

    public List<CompanyDto> getAll() {
        return companyDatabase.getAll().stream()
                .map(companyMapper::toDto).collect(Collectors.toList());
    }

    public CompanyDto update(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        Company returnedCompany = companyDatabase.update(company);
        return companyMapper.toDto(returnedCompany);
    }
}
