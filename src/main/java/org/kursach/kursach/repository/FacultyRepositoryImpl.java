package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Faculty;

import java.util.List;

@ApplicationScoped
public class FacultyRepositoryImpl implements FacultyRepository {
    public FacultyRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Faculty findById(Long id) {
        return em.find(Faculty.class, id);
    }
    
    @Override
    public List<Faculty> findAll() {
        TypedQuery<Faculty> query = em.createQuery("SELECT f FROM Faculty f", Faculty.class);
        return query.getResultList();
    }
    
    @Override
    public List<Faculty> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Faculty> query = em.createQuery(
            "SELECT f FROM Faculty f WHERE LOWER(f.nameFaculty) LIKE :name OR LOWER(f.shortNameFaculty) LIKE :name", 
            Faculty.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public void save(Faculty faculty) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (faculty.getId() == null) {
                em.persist(faculty);
            } else {
                faculty = em.merge(faculty);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении факультета", e);
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
            
            Faculty faculty = findById(id);
            if (faculty != null) {
                em.remove(faculty);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении факультета", e);
        }
    }
} 