package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.dto.ClientDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "phoneNo", target = "phone")
    })
    ClientDto clientToClientDto(Client client);

    @InheritInverseConfiguration
    Client clientDtoToClient(ClientDto clientDto);

    List<ClientDto> clientsToClientDtoList(List<Client> clients);
}
