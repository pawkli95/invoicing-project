package pl.futurecollars.invoicing.service.integrationTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import pl.futurecollars.invoicing.db.companies.CompanyRepository
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.CompanyService
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
class CompanyServiceIntegrationTest extends Specification{

    @Autowired
    CompanyService companyService

    @Autowired
    CompanyRepository companyDatabase

    @Autowired
    CompanyMapper companyMapper

    CompanyDto companyDto = CompanyFixture.getCompanyDto()

    def setup() {
        clearDatabase()
    }

    def "should save company to database"() {
        when:
        CompanyDto returnedCompanyDto = companyService.saveCompany(companyDto)

        then:
        def company = companyDatabase.getById(returnedCompanyDto.getId())
        companyMapper.toDto(company) == returnedCompanyDto
    }

    def "should get company by id"() {
        given:
        CompanyDto returnedCompanyDto = companyService.saveCompany(companyDto)

        when:
        CompanyDto response = companyService.getById(returnedCompanyDto.getId())

        then:
        returnedCompanyDto == response
    }

    def "should throw NoSuchElementException when company doesn't exist"() {
        when:
        companyService.getById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }

    def "should return list of all companies dto"() {
        given:
        def companiesDto = addCompanies(10)

        when:
        def response = companyService.getAll()

        then:
        response.size() == 10
        companiesDto == response
    }

    def "should return empty list when database is empty"() {
        when:
        def response = companyService.getAll()

        then:
        response.isEmpty()
    }

    def "should update company"() {
        given:
        def companyDtoToUpdate = addCompanies(1).get(0);
        def updatedCompanyDto = CompanyFixture.getCompanyDto()
        updatedCompanyDto.setId(companyDtoToUpdate.getId())

        when:
        companyService.update(updatedCompanyDto)

        then:
        def response = companyService.getById(companyDtoToUpdate.getId())
        response == updatedCompanyDto
    }

    def "should throw NoSuchElementException when id of company to update doesn't exist"() {
        given:
        def invalidCompany = CompanyFixture.getCompanyDto()
        invalidCompany.setId(UUID.randomUUID())
        when:
        companyService.update(invalidCompany)

        then:
        thrown(NoSuchElementException)
    }

    def "should delete company from database"() {
        given:
        def companyToDelete = addCompanies(4).get(0)

        when:
        companyService.delete(companyToDelete.getId())

        then:
        def response = companyService.getAll()
        response.size() == 3
    }

    def "should throw NoSuchElementException when id of company to delete doesn't exist"() {
        when:
        companyService.delete(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }

    def clearDatabase() {
        for(Company c : companyDatabase.getAll()) {
            companyDatabase.delete(c.getId())
        }
    }

    def List<CompanyDto> addCompanies(int number) {
        List<CompanyDto> list = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            CompanyDto companyDto1 = CompanyFixture.getCompanyDto()
            list.add(companyService.saveCompany(companyDto1))
        }
        return list
    }
}
