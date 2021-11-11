package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.companies.CompanyDatabase
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.model.Company
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
class CompanyDatabaseTest extends Specification {

    @Autowired
    CompanyDatabase companyDatabase

    def setup() {
        clearDatabase()
    }

    Company company = CompanyFixture.getCompany()

    def "should save company to database"() {
        when:
        Company returnedCompany = companyDatabase.save(company)

        then:
        companyDatabase.getById(returnedCompany.getId()) == company

    }

    def "should get company by id"() {
        given:
        Company returnedCompany = companyDatabase.save(company)

        when:
        Company response = companyDatabase.getById(returnedCompany.getId())

        then:
        returnedCompany == response
    }

    def "should throw NoSuchElementException when id doesn't exist"() {
        given:
        UUID invalidId = UUID.randomUUID()

        when:
        companyDatabase.getById(invalidId)

        then:
        thrown(NoSuchElementException)
    }

    def "should get a list of companies"() {
        given:
        def addedCompanies = addCompanies(5)

        when:
        def response = companyDatabase.getAll()

        then:
        response == addedCompanies
    }

    def "should get empty list when no companies exist in database"() {
        when:
        def response = companyDatabase.getAll()

        then:
        response.isEmpty()
    }

    def "should update company"() {
        given:
        Company companyToUpdate = companyDatabase.save(company)
        Company updatedCompany = CompanyFixture.getCompany()
        updatedCompany.setId(companyToUpdate.getId())

        when:
        companyDatabase.update(updatedCompany)

        then:
        companyDatabase.getById(companyToUpdate.getId()) == updatedCompany
    }

    def "should throw NoSuchElementException when updating nonexistent invoice"() {
        when:
        company.setId(UUID.randomUUID())
        companyDatabase.update(company)

        then:
        thrown(NoSuchElementException)
    }

    def "should delete company"() {
        given:
        Company returnedCompany = companyDatabase.save(company)

        when:
        companyDatabase.delete(returnedCompany.getId())

        then:
        companyDatabase.getAll().isEmpty()
    }

    def clearDatabase() {
        for(Company c : companyDatabase.getAll()) {
            companyDatabase.delete(c.getId());
        }
    }

    def List<Company> addCompanies(int number) {
        List<Company> list = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            Company com = CompanyFixture.getCompany()
            Company returned = companyDatabase.save(com)
            list.add(returned)
        }
        return list
    }
}
