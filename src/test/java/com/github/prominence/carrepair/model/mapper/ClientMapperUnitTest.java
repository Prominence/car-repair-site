package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.dto.ClientDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class ClientMapperUnitTest {

    @Autowired
    private ClientMapper clientMapper;

    @Configuration
    public static class Config {
        @Bean
        public ClientMapper clientMapper() {
            return Mappers.getMapper(ClientMapper.class);
        }
    }

    @Test
    public void whenConvertClientToDto_thenReturnAppropriateClientDto() {
        // given
        Client client = new Client("1", "1", "1", "1");
        client.setId(5L);

        // when
        ClientDto clientDto = clientMapper.clientToClientDto(client);

        // then
        checkConformity(client, clientDto);
    }

    @Test
    public void whenConvertClientWithNullsToDto_thenReturnedDtoWillContainNulls() {
        // given
        Client client = new Client(null, "1", "1", null);

        // when
        ClientDto clientDto = clientMapper.clientToClientDto(client);

        // then
        checkConformity(client, clientDto);
    }

    @Test
    public void whenConvertClientDtoToClient_thenReturnAppropriateClient() {
        //given
        ClientDto clientDto = new ClientDto();
        clientDto.setId(4L);
        clientDto.setFirstName("12");
        clientDto.setMiddleName("32");
        clientDto.setLastName("645");
        clientDto.setPhone("+123123");

        // when
        Client client = clientMapper.clientDtoToClient(clientDto);

        // then
        checkConformity(client, clientDto);
    }

    @Test
    public void whenConvertClientDtoWithNullsToClient_thenReturnedClientWillContainNulls() {
        //given
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName("12");
        clientDto.setPhone("+123123");

        // when
        Client client = clientMapper.clientDtoToClient(clientDto);

        // then
        checkConformity(client, clientDto);
    }

    @Test
    public void whenConvertClientListToClientDtoList_thenReturnDtoList() {
        // given
        List<Client> clientList = Arrays.asList(
                new Client("1", "1", "1", "1"),
                new Client("2", "2", "2", "2"),
                new Client("3", "3", "3", "3"),
                new Client("4", "4", "4", "4"),
                new Client("5", "5", "5", "5"),
                new Client("6", "6", "6", "6")
        );

        // when
        List<ClientDto> clientDtoList = clientMapper.clientsToClientDtoList(clientList);

        // then
        for (int i = 0; i < clientList.size(); i++) {
            checkConformity(clientList.get(i), clientDtoList.get(i));
        }
    }

    @Test
    public void whenConvertNullClient_thenReturnNullDto() {
        assertThat(clientMapper.clientToClientDto(null)).isNull();
    }

    @Test
    public void whenConvertNullClientDto_thenReturnNullClient() {
        assertThat(clientMapper.clientDtoToClient(null)).isNull();
    }

    @Test
    public void whenConvertNullClientList_thenReturnNullDtoList() {
        assertThat(clientMapper.clientsToClientDtoList(null)).isNull();
    }

    private void checkConformity(Client client, ClientDto clientDto) {
        assertThat(client.getId()).isEqualTo(clientDto.getId());
        assertThat(client.getFirstName()).isEqualTo(clientDto.getFirstName());
        assertThat(client.getMiddleName()).isEqualTo(clientDto.getMiddleName());
        assertThat(client.getLastName()).isEqualTo(clientDto.getLastName());
        assertThat(client.getPhoneNo()).isEqualTo(clientDto.getPhone());
    }
}
