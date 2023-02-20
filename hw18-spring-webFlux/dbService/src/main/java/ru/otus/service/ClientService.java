package ru.otus.service;

import ru.otus.domain.Client;
import ru.otus.dto.ClientRequestDto;
import ru.otus.dto.ClientResponseDto;

import java.util.List;

public interface ClientService {

    Client saveClient(ClientRequestDto clientRequestDto);

    List<ClientResponseDto> findAll();
}
