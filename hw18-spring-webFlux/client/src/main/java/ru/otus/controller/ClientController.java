package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.dto.ClientRequestDto;
import ru.otus.dto.ClientResponseDto;

import java.util.List;

@Controller
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final WebClient client;

    public ClientController(WebClient.Builder builder) {
        client = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    @GetMapping({"/", "/client/list"})
    public String getClientList(Model model) {

        Mono<List<ClientResponseDto>> clientResponseDtos = client.get().uri("http://localhost:8081/client/list")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ClientResponseDto>>() {
                })
                .doOnNext(val -> log.info("val: {}", val));
//        List<ClientResponseDto> dtos2 = clientResponseDtos.block();
//        return clientResponseDtos.map(dtos -> {
//            model.addAttribute("clients", dtos);
//            return "clientsList";
//        });
        model.addAttribute("clients", clientResponseDtos);
        return "clientsList";
    }


    @GetMapping("/client/create")
    public String createClient(Model model) {
        model.addAttribute("client", new ClientRequestDto());

        return "clientCreate";
    }

    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute("client") ClientRequestDto clientRequestDto) {

        return "redirect:/client/list";
    }
}
