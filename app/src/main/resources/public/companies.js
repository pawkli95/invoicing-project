const getCompanies = async () => {
const response = await fetch("http://localhost:8080/api/companies")
const companies = await response.json()

const companiesTable = document.getElementById('companies-table')

for(const company of companies) {
const row = companiesTable.insertRow(-1)

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
<<<<<<< HEAD
})
=======
}
>>>>>>> c38a40c (First commit of version2)
}

window.onload = function() {
    getCompanies()
}
