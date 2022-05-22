package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Address;
import ru.otus.repository.AddressRepository;

import java.util.NoSuchElementException;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(() ->
                new NoSuchElementException(String.format("can't find address with id = %s", addressId)));
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
}
