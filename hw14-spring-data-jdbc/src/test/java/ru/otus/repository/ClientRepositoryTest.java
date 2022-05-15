package ru.otus.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {

    private static final Long EXISTING_CLIENT_ID = 1L;
    private static final String EXISTING_CLIENT_NAME = "Ivan";
    private static final Long EXISTING_CLIENT_ADDRESS_ID = 1L;
    private static final Set<Phone> EXISTING_CLIENT_PHONES = Set.of(new Phone(1L, "791111111122", 1L),
                                                                    new Phone(2L, "791111111123", 1L));

    @Autowired
    ClientRepository clientRepository;

    Client client;

    @Test
    void should_save_client() {

        client = new Client("name2", 1L, new HashSet<>());
        Client actualClient = clientRepository.save(client);
        assertThat(actualClient.getName()).isEqualTo(client.getName());
    }

    @Test
    void should_return_list_with_existing_person() {
        Client expectedClient = new Client(EXISTING_CLIENT_ID, EXISTING_CLIENT_NAME, EXISTING_CLIENT_ADDRESS_ID, EXISTING_CLIENT_PHONES);
        List<Client> clients = clientRepository.findAll();
        assertThat(clients)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedClient);
    }
}