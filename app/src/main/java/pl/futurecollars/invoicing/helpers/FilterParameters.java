package pl.futurecollars.invoicing.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.util.Optional;

@Builder
@AllArgsConstructor
public class FilterParameters {

    private LocalDate afterDate;
    private LocalDate beforeDate;
    private String sellerTaxId;
    private String buyerTaxId;

    public Optional<LocalDate> getAfterDate() {
        return Optional.ofNullable(afterDate);
    }

    public Optional<LocalDate> getBeforeDate() {
        return Optional.ofNullable(beforeDate);
    }

    public Optional<String> getSellerTaxId() {
        return Optional.ofNullable(sellerTaxId);
    }

    public Optional<String> getBuyerTaxId() {
        return Optional.ofNullable(buyerTaxId);
    }

}
