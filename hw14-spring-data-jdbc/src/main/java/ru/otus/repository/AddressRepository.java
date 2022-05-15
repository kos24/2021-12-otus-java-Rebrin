package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
