package pl.futurecollars.invoicing.service.unitTests

import org.mapstruct.factory.Mappers

import pl.futurecollars.invoicing.repository.CompanyRepository
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.service.CompanyService
import spock.lang.Specification

class CompanyServiceTest extends Specification {

    CompanyRepository companyRepository

    CompanyService companyService

    CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class)

    CompanyDto companyDto = CompanyFixture.getCompanyDto()

    def setup() {
        companyRepository = Mock()
        companyService = new CompanyService(companyRepository, companyMapper)
    }

    def "calling saveCompany() should map dto to entity and delegate to database save()"() {
        when: "we ask companyService to save company"
        companyService.save(companyDto)

        then: "companyDatabase method save() is called"
        1 * companyRepository.save(companyMapper.toEntity(companyDto))
    }

    def "should return list of companyDto"() {
        given: "a list of companies returned by database"
        companyRepository.findAll() >> List.of(companyMapper.toEntity(companyDto))

        when: "we ask companyService to return a list of companies"
        def list = companyService.getAll()

        then: "list of companies is returned"
        list == [companyDto]
    }

    def "calling getById() should map entity to dto and return it"() {
        given:"a company returned by database"
        UUID id = UUID.randomUUID()
        companyDto.setId(id)
        companyRepository.findById(id) >> Optional.ofNullable(companyMapper.toEntity(companyDto))

        when:"we ask companyService to get company by id"
        def response = companyService.getById(id)

        then: "company mapped to dto is returned"
        response == companyDto
    }

    def "should throw NoSuchElementException when getting company by id if company doesn't exist"() {
        given:
        UUID id = UUID.randomUUID()
        companyRepository.findById(id) >> Optional.ofNullable(null)

        when:
        companyService.getById(id)

        then:
        thrown(NoSuchElementException)
    }

    def "calling update() should map dto to entity and call database update()"() {
        given:
        companyRepository.existsById(companyDto.getId()) >> true

        when:"we ask companyService to update company"
        companyService.update(companyDto)

        then:"database save() method is called"
        1 * companyRepository.save(companyMapper.toEntity(companyDto))
    }

    def "should throw NoSuchElementException when updating company if company doesn't exist"() {
        given:
        UUID id = companyDto.getId()
        companyRepository.existsById(id) >> false

        when:
        companyService.update(companyDto)

        then:
        thrown(NoSuchElementException)
    }

    def "calling delete() should call database delete()"() {
        given: "random UUID"
        UUID id = UUID.randomUUID()
        companyRepository.existsById(id) >> true

        when: "we ask companyService to delete company by id"
        companyService.delete(id)

        then: "database delete() method is called"
        1 * companyRepository.deleteById(id)
    }

    def "should throw NoSuchElementException when deleting company if company doesn't exist"() {
        given:
        UUID id = UUID.randomUUID()
        companyRepository.existsById(id) >> false

        when:
        companyService.delete(id)

        then:
        thrown(NoSuchElementException)
    }
}
