package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Phone;

public interface PhoneRepository extends CrudRepository<Phone, Long> {
}
