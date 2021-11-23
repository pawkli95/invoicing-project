package pl.futurecollars.invoicing.service.companyService
// TODO: MP: analogicznie jak w CompanyController - nazwa pakietu do zmiany
import org.mapstruct.factory.Mappers
import pl.futurecollars.invoicing.db.companies.CompanyDatabase
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.service.CompanyService
import spock.lang.Specification

class CompanyServiceUnitTest extends Specification {

    CompanyDatabase companyDatabase

    CompanyService companyService

    CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class)

    CompanyDto companyDto = CompanyFixture.getCompanyDto()

    def setup() {
        companyDatabase = Mock()
        companyService = new CompanyService(companyDatabase, companyMapper)
    }

    def "calling saveCompany() should map dto to entity and delegate to database save()"() {
        when: "we ask companyService to save company"
        companyService.saveCompany(companyDto)

        then: "companyDatabase method save() is called"
        1 * companyDatabase.save(companyMapper.toEntity(companyDto))
    }

    def "should return list of companyDto"() {
        given: "a list of companies returned by database"
        companyDatabase.getAll() >> List.of(companyMapper.toEntity(companyDto))

        when: "we ask companyService to return a list of companies"
        def list = companyService.getAll()

        then: "list of companies is returned"
        list == [companyDto]
    }

    def "calling getById() should map entity to dto and return it"() {
        given:"a company returned by database"
        UUID id = UUID.randomUUID()
        companyDto.setId(id)
        companyDatabase.getById(id) >> companyMapper.toEntity(companyDto)

        when:"we ask companyService to get company by id"
        def response = companyService.getById(id)

        then: "company mapped to dto is returned"
        response == companyDto
    }

    def "calling update() should map dto to entity and call database update()"() {
        when:"we ask companyService to update company"
        companyService.update(companyDto)

        then:"database update() method is called"
        1 * companyDatabase.update(companyMapper.toEntity(companyDto))
    }

    def "calling delete() should call database delete()"() {
        given: "random UUID"
        UUID id = UUID.randomUUID()

        when: "we ask companyService to delete company by id"
        companyService.delete(id)

        then: "database delete() method is called"
        1 * companyDatabase.delete(id)
    }
}
