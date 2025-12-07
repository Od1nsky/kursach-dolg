package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Client;

import java.util.List;

@ApplicationScoped
public class ClientRepositoryImpl implements ClientRepository {
    public ClientRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Client findById(Long id) {
        return em.find(Client.class, id);
    }
    
    @Override
    public List<Client> findAll() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
        return query.getResultList();
    }
    
    @Override
    public List<Client> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE :name OR LOWER(c.lastName) LIKE :name OR LOWER(c.email) LIKE :name", 
            Client.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public void save(Client client) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (client.getId() == null) {
                em.persist(client);
            } else {
                client = em.merge(client);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении клиента", e);
        }
    }
    
    @Override
    public void delete(Long id) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            Client client = findById(id);
            if (client != null) {
                em.remove(client);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении клиента", e);
        }
    }
}


