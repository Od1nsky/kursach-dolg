package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.repository.ClientRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ClientService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private ClientRepository clientRepository;
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByName(name);
    }
    
    public Client getClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
    
    public void deleteClient(Long id) {
        clientRepository.delete(id);
    }
}



