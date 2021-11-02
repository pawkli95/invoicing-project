const getCompanies = async () => {
const response = await fetch("http://localhost:8080/api/companies")
const companies = response.getJson()

const companiesTable = document.getElementById('companies-table')
companies.forEach(company => {
const row = companiesTable.insertRow()

const idCell = row.insertCell(0)
idCell.innerText = company.id

const taxIdCell = row.insertCell(1)
taxIdCell.innerText = company.taxIdentificationNumber

const nameCell = row.insertCell(2)
nameCell.innerText = company.name

const addressCell = row.insertCell(3)
addressCell.innerText = company.address

const healthInsuranceCell = row.insertCell(4)
healthInsuranceCell.innerText = company.healthInsurance

const pensionInsuranceCell = row.insertCell(5)
pensionInsuranceCell.innerText = company.pensionInsurance
})
}

window.onload = function() {
    getCompanies()
}
