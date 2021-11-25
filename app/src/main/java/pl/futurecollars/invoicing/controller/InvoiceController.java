package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.dto.InvoiceDto;
import pl.futurecollars.invoicing.exceptions.ConstraintException;
import pl.futurecollars.invoicing.helpers.FilterParameters;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.service.InvoiceService;

@CrossOrigin(origins = "http://localhost:4200/")
@Api(tags = {"invoice-controller"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @ApiOperation(value = "Add new invoice")
    @PostMapping
    public ResponseEntity<InvoiceDto> saveInvoice(@RequestBody @Valid InvoiceDto invoice) throws ConstraintException {
        log.debug("Request to save invoice");
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.saveInvoice(invoice));
    }

    @ApiOperation(value = "Get invoices based on parameters")
    @GetMapping(produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<List<InvoiceDto>> getAll(@RequestParam(value = "before", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate before,
                                                @RequestParam(value = "after", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate after,
                                                @RequestParam(value = "sellerTaxId", required = false) String sellerTaxId,
                                                @RequestParam(value = "buyerTaxId", required = false) String buyerTaxId) {
        FilterParameters filterParameters = FilterParameters
                .builder()
                .beforeDate(before)
                .afterDate(after)
                .sellerTaxId(sellerTaxId)
                .buyerTaxId(buyerTaxId)
                .build();
        return ResponseEntity.ok().body(invoiceService.filter(filterParameters));
    }

    @ApiOperation(value = "Get invoice by id")
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getById(@PathVariable UUID id) throws NoSuchElementException {
        log.debug("Request to return invoice by id: " + id.toString());
        return ResponseEntity.ok().body(invoiceService.getById(id));
    }

    @ApiOperation(value = "Update invoice")
    @PutMapping
    public ResponseEntity<InvoiceDto> update(@RequestBody @Valid InvoiceDto updatedInvoice) throws NoSuchElementException {
        log.debug("Request to update invoice");
        updatedInvoice.getInvoiceEntries().forEach(InvoiceEntry::calculateVatValue);
        return ResponseEntity.ok().body(invoiceService.updateInvoice(updatedInvoice));
    }

    @ApiOperation(value = "Delete invoice")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws NoSuchElementException, ConstraintException {
        log.debug("Request to delete invoice with id: " + id.toString());
        invoiceService.deleteInvoice(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
