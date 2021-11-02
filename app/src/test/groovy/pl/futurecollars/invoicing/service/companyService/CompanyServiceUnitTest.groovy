package pl.futurecollars.invoicing.service.companyService

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
        when:
        companyService.saveCompany(companyDto)

        then:
        1 * companyDatabase.save(companyMapper.toEntity(companyDto))
    }

    def "should return list of companyDto"() {
        given:
        companyDatabase.getAll() >> List.of(companyMapper.toEntity(companyDto))

        when:
        def list = companyService.getAll()

        then:
        list == [companyDto]
    }

    def "calling getById() should map entity to dto and return it"() {
        given:
        UUID id = UUID.randomUUID()
        companyDto.setId(id)
        companyDatabase.getById(id) >> companyMapper.toEntity(companyDto)

        when:
        def response = companyService.getById(id)

        then:
        response == companyDto
    }

    def "calling update() should map dto to entity and call database update()"() {
        when:
        companyService.update(companyDto)

        then:
        1 * companyDatabase.update(companyMapper.toEntity(companyDto))
    }

    def "calling delete() should call database delete()"() {
        given:
        UUID id = UUID.randomUUID()

        when:
        companyService.delete(id)

        then:
        1 * companyDatabase.delete(id)
    }
}
