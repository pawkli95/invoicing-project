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
        when: "we ask companyDatabase to save company"
        Company returnedCompany = companyDatabase.save(company)

        then: "company is saved in database"
        companyDatabase.getById(returnedCompany.getId()) == company

    }

    def "should get company by id"() {
        given: "company saved in database"
        Company returnedCompany = companyDatabase.save(company)

        when: "we ask companyDatabase to return company by id"
        Company response = companyDatabase.getById(returnedCompany.getId())

        then: "company is returned"
        returnedCompany == response
    }

    def "should throw NoSuchElementException when id doesn't exist"() {
        given: "random UUID"
        UUID invalidId = UUID.randomUUID()

        when: "we ask companyDatabase to return company by id"
        companyDatabase.getById(invalidId)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }

    def "should get a list of companies"() {
        given: "5 companies saved to database"
        def addedCompanies = addCompanies(5)

        when: "we ask companyDatabase to return a list of all companies"
        def response = companyDatabase.getAll()

        then: "list of companies is returned"
        response == addedCompanies
    }

    def "should get empty list when no companies exist in database"() {
        when: "we ask companyDatabase to return a list of all companies"
        def response = companyDatabase.getAll()

        then: "empty list is returned"
        response.isEmpty()
    }

    def "should update company"() {
        given: "an updated company"
        Company companyToUpdate = companyDatabase.save(company)
        Company updatedCompany = CompanyFixture.getCompany()
        updatedCompany.setId(companyToUpdate.getId())

        when: "we ask companyDatabase to update company"
        companyDatabase.update(updatedCompany)

        then: "company is updated"
        companyDatabase.getById(companyToUpdate.getId()) == updatedCompany
    }

    def "should throw NoSuchElementException when updating nonexistent invoice"() {
        when: "we ask companyDatabase to update company"
        company.setId(UUID.randomUUID())
        companyDatabase.update(company)

        then: "NoSuchElementException is thrown"
        thrown(NoSuchElementException)
    }

    def "should delete company"() {
        given: "database with 1 company"
        Company returnedCompany = companyDatabase.save(company)

        when: "we ask companyDatabase to delete company by id"
        companyDatabase.delete(returnedCompany.getId())

        then: "database is empty"
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
