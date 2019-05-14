package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.dto.ClientDto;
import com.github.prominence.carrepair.model.mapper.ClientMapper;
import com.github.prominence.carrepair.repository.ClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private static final Logger logger = LogManager.getLogger(ClientService.class);

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public Page<Client> findAll(Pageable pageable) {
        final Page<Client> clientPage = clientRepository.findAll(pageable);
        logger.trace("Original page: {}.", () -> clientPage);
        return clientPage;
    }

    public Optional<Client> findById(Long id) {
        final Optional<Client> clientOptional = clientRepository.findById(id);
        logger.debug("{} found by id={}", () -> clientOptional, () -> id);
        return clientOptional;
    }

    public Client save(Client client) {
        final Client clientToSave = clientRepository.save(client);
        logger.trace("[{}] was saved.", () -> clientToSave);
        return clientToSave;
    }

    public Client save(ClientDto clientDto) {
        return save(clientMapper.clientDtoToClient(clientDto));
    }

    public boolean deleteClientById(Long id) {
        try {
            clientRepository.deleteById(id);
            logger.debug("Client[id={}] was deleted.", () -> id);
            return true;
        } catch (Exception e) {
            logger.error("Client[id={}] wasn't deleted. Exception: {}", () -> id, e::getMessage);
            return false;
        }
    }

    public long getClientCount() {
        final long clientCount = clientRepository.count();
        logger.trace("Found {} clients.", () -> clientCount);
        return clientCount;
    }

    public List<Client> searchByInitials(String query) {
        final List<Client> allByInitials = clientRepository.findAllByInitials(query);
        logger.debug("Found {} clients by initials: {}.", allByInitials::size, () -> query);
        return allByInitials;
    }

    public Page<ClientDto> convertToDtoPage(Page<Client> clientPage) {
        final Page<ClientDto> clientDtoPage = new PageImpl<> (clientMapper.clientsToClientDtoList(clientPage.getContent()),
                clientPage.getPageable(), clientPage.getTotalElements());
        logger.trace("Dto page: {}.", () -> clientDtoPage);
        return clientDtoPage;
    }

    public ClientDto convertToDto(Client client) {
        return clientMapper.clientToClientDto(client);
    }

    public List<ClientDto> convertToDtoList(List<Client> clientList) {
        return clientMapper.clientsToClientDtoList(clientList);
    }
}
