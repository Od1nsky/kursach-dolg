package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Faculty;

import java.util.List;

@ApplicationScoped
public class ChairRepositoryImpl implements ChairRepository {
    public ChairRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Chair findById(Long id) {
        return em.find(Chair.class, id);
    }
    
    @Override
    public List<Chair> findAll() {
        TypedQuery<Chair> query = em.createQuery("SELECT c FROM Chair c", Chair.class);
        return query.getResultList();
    }
    
    @Override
    public List<Chair> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Chair> query = em.createQuery(
            "SELECT c FROM Chair c WHERE LOWER(c.nameChair) LIKE :name OR LOWER(c.shortNameChair) LIKE :name", 
            Chair.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public List<Chair> findByFaculty(Faculty faculty) {
        return em.createQuery("SELECT c FROM Chair c WHERE c.faculty = :faculty", Chair.class)
                 .setParameter("faculty", faculty)
                 .getResultList();
    }
    
    @Override
    public void save(Chair chair) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (chair.getId() == null) {
                em.persist(chair);
            } else {
                chair = em.merge(chair);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении кафедры", e);
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
            
            Chair chair = findById(id);
            if (chair != null) {
                em.remove(chair);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении кафедры", e);
        }
    }
} 