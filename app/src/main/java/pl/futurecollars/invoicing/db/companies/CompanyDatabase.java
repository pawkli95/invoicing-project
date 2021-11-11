package pl.futurecollars.invoicing.db.companies;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.model.Company;

@Slf4j
@RequiredArgsConstructor
public class CompanyDatabase implements Database<Company> {

    private final CompanyRepository companyRepository;

    @Override
    public Company save(Company company) {
        try {
            return companyRepository.save(company);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException("This tax id is already in use");
        }
    }

    @Override
    public Company getById(UUID id) throws NoSuchElementException {
        Optional<Company> optional = companyRepository.findById(id);
        return optional.orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company update(Company updatedEntity) {
        if (companyRepository.existsById(updatedEntity.getId())) {
            return companyRepository.save(updatedEntity);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        try {
            if (companyRepository.existsById(id)) {
                companyRepository.deleteById(id);
            } else {
                throw new NoSuchElementException();
            }
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException("To delete company, delete related invoices first");
        }
    }
}
