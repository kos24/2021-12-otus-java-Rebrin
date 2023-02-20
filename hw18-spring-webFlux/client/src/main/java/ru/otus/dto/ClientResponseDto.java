package ru.otus.dto;

import ru.otus.domain.Phone;

import java.util.Set;

public class ClientResponseDto {

    private Long id;
    private String name;
    private String street;
    private Set<Phone> phones;

    public ClientResponseDto(Long id, String name, String street, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.phones = phones;
    }

    public ClientResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public Set<Phone> getPhones() {
        return phones;
    }
}
