const getCompanies = async () => {
const response = await fetch("http://localhost:8080/api/companies")
const companies = await response.json()

const companiesTable = document.getElementById('companies-table')

for(const company of companies) {
const row = companiesTable.insertRow(-1)

const idCell = row.insertCell(0)
idCell.innerText = company.id

const nameCell = row.insertCell(1)
nameCell.innerText = company.name

const taxIdCell = row.insertCell(2)
taxIdCell.innerText = company.taxIdentificationNumber

const addressCell = row.insertCell(3)
addressCell.innerText = company.address

const healthInsuranceCell = row.insertCell(4)
healthInsuranceCell.innerText = company.healthInsurance

const pensionInsuranceCell = row.insertCell(5)
pensionInsuranceCell.innerText = company.pensionInsurance
}
}

window.onload = function() {
    getCompanies()
    const form = document.getElementById('form')
    form.addEventListener('submit', addCompany);
}

function addCompany(event) {
event.preventDefault();

const data = new FormData(event.target)

const object = Object.fromEntries(data.entries())

const r = sendObject("http://localhost:8080/api/companies", object)

}

function sendObject(link, object) {
const r = fetch(link , {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(object),
})}
