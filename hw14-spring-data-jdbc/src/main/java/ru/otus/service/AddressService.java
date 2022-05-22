package ru.otus.service;

import ru.otus.domain.Address;

public interface AddressService {
    Address getAddressById(Long addressId);

    Address saveAddress(Address address);
}
