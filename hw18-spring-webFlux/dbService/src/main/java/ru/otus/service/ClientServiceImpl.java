package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;
import ru.otus.dto.ClientRequestDto;
import ru.otus.dto.ClientResponseDto;
import ru.otus.repository.ClientRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AddressService addressService;
    private final PhoneService phoneService;

    public ClientServiceImpl(ClientRepository clientRepository, AddressService addressService, PhoneService phoneService) {
        this.clientRepository = clientRepository;
        this.addressService = addressService;
        this.phoneService = phoneService;
    }

    @Override
    public Client saveClient(ClientRequestDto clientRequestDto) {
        Address address = addressService.saveAddress(new Address(clientRequestDto.getStreet()));
        Client client = clientRepository.save(new Client(clientRequestDto.getName(), address.getId(), new HashSet<>()));
        phoneService.save(new Phone(clientRequestDto.getPhoneNumber(), client.getId()));
        return client;
    }

    @Override
    public List<ClientResponseDto> findAll() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(client -> {
            String street = addressService.getAddressById(client.getAddressId()).getStreet();
            return new ClientResponseDto(client.getId(), client.getName(), street, client.getPhones());
        }).toList();
    }
}
