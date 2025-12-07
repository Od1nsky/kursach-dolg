package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Service;

import java.util.List;

@ApplicationScoped
public class ServiceRepositoryImpl implements ServiceRepository {
    public ServiceRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Service findById(Long id) {
        return em.find(Service.class, id);
    }
    
    @Override
    public List<Service> findAll() {
        TypedQuery<Service> query = em.createQuery("SELECT s FROM Service s", Service.class);
        return query.getResultList();
    }
    
    @Override
    public List<Service> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Service> query = em.createQuery(
            "SELECT s FROM Service s WHERE LOWER(s.name) LIKE :name", 
            Service.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public void save(Service service) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (service.getId() == null) {
                em.persist(service);
            } else {
                service = em.merge(service);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении услуги", e);
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
            
            Service service = findById(id);
            if (service != null) {
                em.remove(service);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении услуги", e);
        }
    }
}


