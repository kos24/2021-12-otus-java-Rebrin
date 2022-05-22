package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;
import ru.otus.dto.ClientRequestDto;
import ru.otus.dto.ClientResponseDto;
import ru.otus.service.AddressService;
import ru.otus.service.ClientService;
import ru.otus.service.PhoneService;

import java.util.HashSet;
import java.util.List;

@Controller
public class ClientController {

    private final ClientService clientService;
    private final AddressService addressService;
    private final PhoneService phoneService;

    public ClientController(ClientService clientService, AddressService addressService, PhoneService phoneService) {
        this.clientService = clientService;
        this.addressService = addressService;
        this.phoneService = phoneService;
    }

    @GetMapping({"/", "/client/list"})
    public String getClientList(Model model) {
        List<Client> clients = clientService.findAll();
        List<ClientResponseDto> clientResponseDtos = clients.stream().map(client -> {
            String street = addressService.getAddressById(client.getAddressId()).getStreet();
            return new ClientResponseDto(client.getId(), client.getName(), street, client.getPhones());
        }).toList();
        if (!clients.isEmpty()) {
            model.addAttribute("clients", clientResponseDtos);
        }
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String createClient(Model model) {
        model.addAttribute("client", new ClientRequestDto());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute("client") ClientRequestDto clientRequestDto) {
        Address address = addressService.saveAddress(new Address(clientRequestDto.getStreet()));
        Client client = clientService.saveClient(new Client(clientRequestDto.getName(), address.getId(), new HashSet<>()));
        phoneService.save(new Phone(clientRequestDto.getPhoneNumber(), client.getId()));
        return "redirect:/client/list";
    }
}
