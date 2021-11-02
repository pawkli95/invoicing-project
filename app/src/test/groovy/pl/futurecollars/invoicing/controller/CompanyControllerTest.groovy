package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("jpaTest")
class CompanyControllerTest extends Specification {

    @Autowired
    CompanyController companyController

    @Autowired
    MockMvc mockMvc

    CompanyDto companyDto = CompanyFixture.getCompanyDto()

    @Autowired
    JacksonTester<CompanyDto> jsonService

    @Autowired
    JacksonTester<List<CompanyDto>> jsonListService

    def setup() {
        clearDatabase()
    }

    def "should save company to database"() {
        given:
        String jsonString = jsonService.write(companyDto).getJson()

        when:
        def response = mockMvc
                .perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        def company = jsonService.parseObject(response)
        company.getTaxIdentificationNumber() == companyDto.getTaxIdentificationNumber()
        company.getName() == companyDto.getName()
    }

    def "should return company by id"() {
        given:
        def returnedCompaniesDto = addCompanies(5)
        CompanyDto returnedCompanyDto = returnedCompaniesDto.get(0)
        UUID id = returnedCompanyDto.getId()

        when:
        def response = mockMvc
                .perform(get("/api/companies/" + id.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        jsonService.parseObject(response) == returnedCompanyDto
    }

    def "should return 404 NotFound status when getting company by nonexistent id"() {
        expect:
        mockMvc
                .perform(get("/api/companies/"+ UUID.randomUUID().toString()))
                .andExpect(status().isNotFound())
    }

    def "should return list of companies"() {
        given:
        def returnedList = addCompanies(10)

        when:
        def response = mockMvc
                .perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        def list = jsonListService.parseObject(response)
        list.size() == 10
        list == returnedList
    }

    def "should update company details"() {
        given:
        def returnedCompany = addCompany()
        def updatedCompany = CompanyFixture.getCompanyDto()
        updatedCompany.setId(returnedCompany.getId())
        String jsonString = jsonService.write(updatedCompany).getJson()

        when:
        def response = mockMvc
                .perform(put("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()

        then:
        jsonService.parseObject(response) == updatedCompany
    }

    def "should delete company"() {
        given:
        def list = addCompanies(10)
        def companyToDelete = list.get(0)
        UUID id = companyToDelete.getId()

        when:
        mockMvc
                .perform(delete("/api/companies/" + id.toString()))
                .andExpect(status().isAccepted())

        then:
        getAllCompanies().size() == 9
    }

    def "should return 404 NotFound status when deleting company with nonexistent id"() {
        expect:
        mockMvc
                .perform(delete("/api/companies/" + UUID.randomUUID().toString()))
                .andExpect(status().isNotFound())
    }

    def clearDatabase() {
        for(CompanyDto c : getAllCompanies()) {
            deleteCompany(c.getId())
        }
    }

    def getAllCompanies() {
        def list = mockMvc
                .perform(get("/api/companies"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        return jsonListService.parseObject(list)
    }

    def deleteCompany(UUID id) {
        mockMvc.perform(delete("/api/companies/" + id.toString()))
    }

    def List<CompanyDto> addCompanies(int number) {
        List<CompanyDto> list = new ArrayList<>()
        for(int i = 0; i < number; i++) {
           CompanyDto companyDto1 = addCompany()
            list.add(companyDto1)
        }
        return list
    }

    def CompanyDto addCompany() {
        CompanyDto c = CompanyFixture.getCompanyDto()
        String jsonString = jsonService.write(c).getJson()
        def response = mockMvc
                .perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andReturn()
                .getResponse()
                .getContentAsString()
        return jsonService.parseObject(response)
    }


}
