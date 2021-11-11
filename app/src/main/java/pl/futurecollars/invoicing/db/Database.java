package pl.futurecollars.invoicing.db;

import pl.futurecollars.invoicing.exceptions.ConstraintException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface Database<T> {

    T save(T entity);

    T getById(UUID id) throws NoSuchElementException;

    List<T> getAll();

    T update(T updatedEntity);

    void delete(UUID id) throws NoSuchElementException;
}
