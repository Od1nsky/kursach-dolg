package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.model.Discipline;

import java.util.List;

@ApplicationScoped
public class DisciplineRepositoryImpl implements DisciplineRepository {
    public DisciplineRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Discipline findById(Long id) {
        return em.find(Discipline.class, id);
    }
    
    @Override
    public List<Discipline> findAll() {
        TypedQuery<Discipline> query = em.createQuery("SELECT d FROM Discipline d", Discipline.class);
        return query.getResultList();
    }
    
    @Override
    public List<Discipline> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Discipline> query = em.createQuery(
            "SELECT d FROM Discipline d WHERE LOWER(d.nameDiscipline) LIKE :name", 
            Discipline.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public List<Discipline> findByChair(Chair chair) {
        return em.createQuery("SELECT d FROM Discipline d WHERE d.chair = :chair", Discipline.class)
                 .setParameter("chair", chair)
                 .getResultList();
    }
    
    @Override
    public List<Discipline> findByCurriculum(Curriculum curriculum) {
        return em.createQuery("SELECT d FROM Discipline d WHERE d.curriculum = :curriculum", Discipline.class)
                 .setParameter("curriculum", curriculum)
                 .getResultList();
    }
    
    @Override
    public List<Discipline> findByCourseAndSemester(Integer course, Integer semester) {
        return em.createQuery("SELECT d FROM Discipline d WHERE d.course = :course AND d.semester = :semester", Discipline.class)
                 .setParameter("course", course)
                 .setParameter("semester", semester)
                 .getResultList();
    }
    
    @Override
    public void save(Discipline discipline) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (discipline.getId() == null) {
                em.persist(discipline);
            } else {
                discipline = em.merge(discipline);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении дисциплины", e);
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
            
            Discipline discipline = findById(id);
            if (discipline != null) {
                em.remove(discipline);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении дисциплины", e);
        }
    }
} 