package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.dto.ClientDto;
import com.github.prominence.carrepair.model.mapper.ClientMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(ClientService.class)
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientMapper clientMapper;

    @Before
    public void setup() {
        Arrays.asList(
                new Client("1", "1", "1", "1"),
                new Client("2", "2", "2", "2"),
                new Client("3", "3", "3", "3")
        ).forEach(clientService::save);
    }

    @Test
    public void whenSaveValidClient_thenHeWillBePersisted() {
        // given
        Client testClient = getTestClient();

        // when
        testClient = clientService.save(testClient);

        // then
        assertThat(testClient.getId()).isPositive();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenSaveInvalidClient_thenThrowAnException() {
        // given
        Client testClient = getTestClient();
        testClient.setFirstName(null);

        // expected exception
        clientService.save(testClient);
    }

    @Test
    public void whenSaveValidClientDto_thenConvertToOClientAndSave() {
        // given
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName("1");
        clientDto.setMiddleName("1");
        clientDto.setLastName("1");
        clientDto.setPhone("1");

        when(clientMapper.clientDtoToClient(clientDto))
                .thenReturn(new Client("1", "1", "1", "1"));

        // when
        clientService.save(clientDto);

        // then
        verify(clientMapper, times(1)).clientDtoToClient(clientDto);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenSaveInvalidClientDto_thenThrowAnException() {
        // given
        ClientDto clientDto = new ClientDto();

        when(clientMapper.clientDtoToClient(clientDto))
                .thenReturn(new Client());

        // expected exception
        clientService.save(clientDto);
    }

    @Test
    public void whenFindAll_thenReturnAllSavedClients() {
        assertThat(clientService.findAll(PageRequest.of(0, 10))).isNotEmpty();
    }

    @Test
    public void whenFindById_thenReturnClientWithThisId() {
        // given
        Client testClient = getTestClient();

        // when
        Long testClientId = clientService.save(testClient).getId();
        Client returnedClient = clientService.findById(testClientId).get();

        // then
        assertThat(returnedClient).isNotNull();
        assertThat(returnedClient.getId()).isEqualTo(testClientId);
    }

    @Test
    public void whenGetClientCount_thenReturnPositiveClientCount() {
        // when
        Long clientCount = clientService.getClientCount();

        // then
        assertThat(clientCount).isPositive();
    }

    @Test
    public void whenNoSavedClientsAndGetClientCount_thenReturnZero() {
        // given
        clientService.deleteAll();

        // when
        Long clientCount = clientService.getClientCount();

        // then
        assertThat(clientCount).isZero();
    }

    @Test
    public void whenSearchByInitials_thenReturnAllThatMatches() {
        // given
        Arrays.asList(
                new Client("John", "1", "Smith", "1"),
                new Client("Ivan", "2", "Ivanov", "2"),
                new Client("Alexey", "Ivanovich", "Smith", "3")
        ).forEach(clientService::save);

        // when
        List<Client> foundClients = clientService.searchByInitials("Ivan");

        // then
        assertThat(foundClients.size()).isEqualTo(2);

        // when
        foundClients = clientService.searchByInitials("Smith");

        // then
        assertThat(foundClients.size()).isEqualTo(2);

        // when
        foundClients = clientService.searchByInitials("Jo");

        // then
        assertThat(foundClients.size()).isEqualTo(1);

        // when
        foundClients = clientService.searchByInitials("noone");

        // then
        assertThat(foundClients.size()).isZero();
    }

    @Test
    public void whenDeleteClientById_thenHeWillBeDeleted() {
        // given
        Client testClient = getTestClient();

        // when
        Long savedClientId = clientService.save(testClient).getId();
        boolean wasDeleted = clientService.deleteClientById(savedClientId);

        // then
        assertThat(wasDeleted).isTrue();
        assertThat(clientService.findById(savedClientId).isPresent()).isFalse();
    }

    @Test
    public void whenConvertToDto_thenMapperIsParticipating() {
        // given
        Client testClient = getTestClient();

        // when
        clientService.convertToDto(testClient);

        // then
        verify(clientMapper, times(1)).clientToClientDto(testClient);
    }

    @Test
    public void whenConvertClientPageToDtoPage_thenMapperIsParticipating() {
        // given
        Page<Client> clientPage = clientService.findAll(PageRequest.of(0, 10));

        // when
        clientService.convertToDtoPage(clientPage);

        // then
        verify(clientMapper, times(1)).clientsToClientDtoList(clientPage.getContent());
    }

    @Test
    public void whenConvertClientListToDtoList_thenMapperIsParticipating() {
        // given
        List<Client> clients = Collections.singletonList(getTestClient());

        // when
        clientService.convertToDtoList(clients);

        // then
        verify(clientMapper, times(1)).clientsToClientDtoList(clients);
    }

    private Client getTestClient() {
        return new Client("firstName", "middleName", "lastName", "phoneNo");
    }
}
