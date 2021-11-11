package pl.futurecollars.invoicing.exceptions;

public class ConstraintException extends RuntimeException {

    public ConstraintException(String message) {
        super(message);
    }
}
