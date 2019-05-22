package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void whenFindById_thenReturnClient() {
        // given
        Client client = new Client("firstName", "middleName", "lastName", "123456789");
        entityManager.persist(client);
        entityManager.flush();

        // when
        Optional<Client> found = clientRepository.findById(client.getId());

        // then
        assertThat(found).isPresent().hasValue(client)
                .map(Client::hashCode).get().isEqualTo(client.hashCode());
    }

    @Test
    public void whenFindByWrongId_thenReturnNull() {
        // when
        Optional<Client> found = clientRepository.findById(-5L);

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    public void whenFindByInitials_thenReturnList() {
        // given
        Stream.of(
                new Client("Alexey", "MiddleName", "Zinchenko", "12342312"),
                new Client("John", "MiddleName", "Smith", "123123"),
                new Client("Dirk", "Another", "Surname", "1231213")
        ).forEach(entityManager::persist);
        entityManager.flush();

        // when
        List<Client> clientsByMiddleName = clientRepository.findAllByInitials("Name");

        // then
        assertThat(clientsByMiddleName).size().isEqualTo(2);

        // when
        List<Client> clientsByNonExistingInfo = clientRepository.findAllByInitials("Ivan");

        // then
        assertThat(clientsByNonExistingInfo).isEmpty();

        // when
        List<Client> clientsByName = clientRepository.findAllByInitials("Alexey");

        // then
        assertThat(clientsByName).isNotEmpty().size().isEqualTo(1);
    }
}

