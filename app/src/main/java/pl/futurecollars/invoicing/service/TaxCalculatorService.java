package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.dto.TaxCalculation;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.repository.CompanyRepository;
import pl.futurecollars.invoicing.repository.InvoiceRepository;

@Slf4j
@Service
@AllArgsConstructor
public class TaxCalculatorService {

    private final InvoiceRepository invoiceRepository;

    private final CompanyRepository companyRepository;

    public TaxCalculation getTaxCalculation(String taxId) throws NoSuchElementException {
        Optional<Company> optional = checkForTaxId(taxId);
        return mapToTaxCalculation(optional
                .orElseThrow(() -> new NoSuchElementException("No company with such tax id exists")));
    }

    private TaxCalculation mapToTaxCalculation(Company company) {
        return TaxCalculation.builder()
                .income(income(company))
                .costs(costs(company))
                .incomeMinusCosts(incomeMinusCosts(company))
                .incomingVat(incomingVat(company))
                .outgoingVat(outgoingVat(company))
                .vatToReturn(vatToReturn(company))
                .pensionInsurance(company.getPensionInsurance())
                .incomeMinusCostsMinusPensionInsurance(incomeMinusCostsMinusPensionInsurance(company))
                .taxCalculationBase(taxCalculationBase(company))
                .incomeTax(incomeTax(company))
                .healthInsurance9(healthInsurance_9(company))
                .healthInsurance775(healthInsurance_7_75(company))
                .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance(company))
                .finalIncomeTaxValue(finalIncomeTaxValue(company))
                .build();
    }

    private BigDecimal calculate(Predicate<Invoice> predicate, Function<InvoiceEntry, BigDecimal> calculationFunction) {
        return invoiceRepository.findAll().stream()
                .filter(predicate)
                .flatMap(invoice -> invoice.getInvoiceEntries().stream())
                .peek(InvoiceEntry::calculateVatValue)
                .map(calculationFunction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal income(Company company) {
        Predicate<Invoice> predicate = invoice -> invoice.getSeller().getTaxIdentificationNumber()
                .equals(company.getTaxIdentificationNumber());
        return calculate(predicate, InvoiceEntry::getPrice);
    }

    private BigDecimal costs(Company company) {
        Predicate<Invoice> predicate = invoice -> invoice.getBuyer().getTaxIdentificationNumber()
                .equals(company.getTaxIdentificationNumber());
        return calculate(predicate, InvoiceEntry::getPrice).add(personalCarCosts(predicate));
    }

    private BigDecimal incomingVat(Company company) {
        Predicate<Invoice> predicate = invoice -> invoice.getSeller().getTaxIdentificationNumber()
                .equals(company.getTaxIdentificationNumber());
        return calculate(predicate, InvoiceEntry::getVatValue);
    }

    private BigDecimal outgoingVat(Company company) {
        Predicate<Invoice> predicate = invoice -> invoice.getBuyer().getTaxIdentificationNumber()
                .equals(company.getTaxIdentificationNumber());
        return personalCarRelatedVat(predicate).add(notPersonalCarRelatedVat(predicate));
    }

    private BigDecimal personalCarRelatedValue(Predicate<Invoice> predicate) {
        return invoiceRepository.findAll()
                .stream()
                .filter(predicate)
                .flatMap(i -> i.getInvoiceEntries().stream())
                .peek(InvoiceEntry::calculateVatValue)
                .filter(InvoiceEntry::isPersonalCar)
                .map(InvoiceEntry::getVatValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal notPersonalCarRelatedVat(Predicate<Invoice> predicate) {
        return invoiceRepository.findAll()
                .stream()
                .filter(predicate)
                .flatMap(i -> i.getInvoiceEntries().stream())
                .peek(InvoiceEntry::calculateVatValue)
                .filter(entry -> !entry.isPersonalCar())
                .map(InvoiceEntry::getVatValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal personalCarRelatedVat(Predicate<Invoice> predicate) {
        return (personalCarRelatedValue(predicate).divide(BigDecimal.valueOf(2))).setScale(2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal personalCarCosts(Predicate<Invoice> predicate) {
        return (personalCarRelatedValue(predicate).divide(BigDecimal.valueOf(2))).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal incomeMinusCosts(Company company) {
        return income(company).subtract(costs(company));
    }

    private BigDecimal vatToReturn(Company company) {
        return incomingVat(company).subtract(outgoingVat(company));
    }

    private BigDecimal incomeMinusCostsMinusPensionInsurance(Company company) {
        BigDecimal pensionInsurance = company.getPensionInsurance();
        return incomeMinusCosts(company).subtract(pensionInsurance);
    }

    private BigDecimal taxCalculationBase(Company company) {
        return incomeMinusCostsMinusPensionInsurance(company).setScale(0, RoundingMode.HALF_DOWN);
    }

    private BigDecimal incomeTax(Company company) {
        return taxCalculationBase(company).multiply(BigDecimal.valueOf(0.19))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal healthInsurance_9(Company company) {
        return company.getHealthInsurance().multiply(BigDecimal.valueOf(0.09)).setScale(2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal healthInsurance_7_75(Company company) {
        return company.getHealthInsurance().multiply(BigDecimal.valueOf(0.0775)).setScale(2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal incomeTaxMinusHealthInsurance(Company company) {
        return incomeTax(company).subtract(healthInsurance_7_75(company));
    }

    private BigDecimal finalIncomeTaxValue(Company company) {
        return incomeTaxMinusHealthInsurance(company).setScale(0, RoundingMode.HALF_DOWN);
    }

    private Optional<Company> checkForTaxId(String taxId) {
        return companyRepository.findAll()
                .stream()
                .filter(c -> Objects.equals(c.getTaxIdentificationNumber(), taxId))
                .findAny();
    }
}
