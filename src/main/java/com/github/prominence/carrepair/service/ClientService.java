package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.repository.ClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private static final Logger logger = LogManager.getLogger(ClientService.class);

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> findAll(Pageable pageable) {
        final Page<Client> clientPage = clientRepository.findAll(pageable);
        logger.trace(clientPage);
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
}
