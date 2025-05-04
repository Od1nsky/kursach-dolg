package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Curriculum;

import java.util.List;

@ApplicationScoped
public class CurriculumRepositoryImpl implements CurriculumRepository {
    public CurriculumRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Curriculum findById(Long id) {
        return em.find(Curriculum.class, id);
    }
    
    @Override
    public List<Curriculum> findAll() {
        TypedQuery<Curriculum> query = em.createQuery("SELECT c FROM Curriculum c", Curriculum.class);
        return query.getResultList();
    }
    
    @Override
    public List<Curriculum> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Curriculum> query = em.createQuery(
            "SELECT c FROM Curriculum c WHERE LOWER(c.nameCurriculum) LIKE :name OR LOWER(c.speciality) LIKE :name", 
            Curriculum.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public List<Curriculum> findByCourse(Integer course) {
        return em.createQuery("SELECT c FROM Curriculum c WHERE c.course = :course", Curriculum.class)
                 .setParameter("course", course)
                 .getResultList();
    }
    
    @Override
    public List<Curriculum> findBySpeciality(String speciality) {
        return em.createQuery("SELECT c FROM Curriculum c WHERE c.speciality LIKE :speciality", Curriculum.class)
                 .setParameter("speciality", "%" + speciality + "%")
                 .getResultList();
    }
    
    @Override
    public void save(Curriculum curriculum) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (curriculum.getId() == null) {
                em.persist(curriculum);
            } else {
                curriculum = em.merge(curriculum);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении учебного плана", e);
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
            
            Curriculum curriculum = findById(id);
            if (curriculum != null) {
                em.remove(curriculum);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении учебного плана", e);
        }
    }
} 