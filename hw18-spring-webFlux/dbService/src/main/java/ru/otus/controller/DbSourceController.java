package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.domain.Client;
import ru.otus.dto.ClientResponseDto;
import ru.otus.service.ClientService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DbSourceController {

    private static final Logger log = LoggerFactory.getLogger(DbSourceController.class);

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ClientService clientService;

    public DbSourceController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ClientResponseDto>> getClients() {
        log.info("Received request - getClients");
        log.info("Request processed.");
        CompletableFuture<List<ClientResponseDto>> future = CompletableFuture
                .supplyAsync(clientService::findAll, executor);
        return Mono.fromFuture(future);
    }

    @PostMapping(value = "/client/save")
    public Mono<Client> saveClient() {

    }
}
