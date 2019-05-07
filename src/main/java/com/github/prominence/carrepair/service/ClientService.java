package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public boolean deleteClientById(Long id) {
        try {
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getClientCount() {
        return clientRepository.count();
    }

    public List<Long> getAllClientIds() {
        return clientRepository.findAllClientIds();
    }

    public List<Client> searchByInitials(String query) {
        return clientRepository.findAllByInitials(query);
    }
}
