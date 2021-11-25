package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyDto saveCompany(CompanyDto companyDto) {
        if (companyRepository.existsByTaxIdentificationNumber(companyDto.getTaxIdentificationNumber())) {
            throw new ConstraintException("This tax identification number is already in use");
        }
        Company company = companyMapper.toEntity(companyDto);
        return companyMapper.toDto(companyRepository.save(company));
    }

    public CompanyDto getById(UUID id) throws NoSuchElementException {
        if (!companyRepository.existsById(id)) {
            throw new NoSuchElementException("Company doesn't exist");
        }
        Company company = companyRepository.getById(id);
        return companyMapper.toDto(company);
    }

    public void delete(UUID id) throws NoSuchElementException, ConstraintException {
        if (!companyRepository.existsById(id)) {
            throw new NoSuchElementException("Company doesn't exist");
        }
        try {
            companyRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException("To delete company, delete related invoices first");
        }
    }

    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toDto).collect(Collectors.toList());
    }

    public CompanyDto update(CompanyDto companyDto) {
        if (!companyRepository.existsById(companyDto.getId())) {
            throw new NoSuchElementException("Company doesn't exist");
        }
        Company company = companyMapper.toEntity(companyDto);
        Company returnedCompany = companyRepository.save(company);
        return companyMapper.toDto(returnedCompany);
    }
}
