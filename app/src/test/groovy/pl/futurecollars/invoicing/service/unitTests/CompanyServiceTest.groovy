package pl.futurecollars.invoicing.service.unitTests

import org.mapstruct.factory.Mappers

import pl.futurecollars.invoicing.db.companies.CompanyRepository
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.service.CompanyService
import spock.lang.Specification

class CompanyServiceTest extends Specification {

    CompanyRepository companyDatabase

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
        companyDatabase.findAll() >> List.of(companyMapper.toEntity(companyDto))

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
        companyDatabase.existsById(id) >> true

        when:"we ask companyService to get company by id"
        def response = companyService.getById(id)

        then: "company mapped to dto is returned"
        response == companyDto
    }

    def "calling update() should map dto to entity and call database update()"() {
        given:
        companyDatabase.existsById(companyDto.getId())

        when:"we ask companyService to update company"
        companyService.update(companyDto)

        then:"database save() method is called"
        1 * companyDatabase.save(companyMapper.toEntity(companyDto))
    }

    def "calling delete() should call database delete()"() {
        given: "random UUID"
        UUID id = UUID.randomUUID()
        companyDatabase.existsById(id) >> true

        when: "we ask companyService to delete company by id"
        companyService.delete(id)

        then: "database delete() method is called"
        1 * companyDatabase.deleteById(id)
    }
}
