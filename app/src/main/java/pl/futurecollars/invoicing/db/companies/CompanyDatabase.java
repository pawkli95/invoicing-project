package pl.futurecollars.invoicing.db.companies;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

@RequiredArgsConstructor
public class CompanyDatabase implements Database<Company> {

    private final CompanyRepository companyRepository;

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
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
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
        } else {
            throw new NoSuchElementException();
        }
    }
}
