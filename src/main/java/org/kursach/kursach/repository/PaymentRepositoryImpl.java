package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Payment;

import java.util.List;

@ApplicationScoped
public class PaymentRepositoryImpl implements PaymentRepository {
    public PaymentRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Payment findById(Long id) {
        return em.find(Payment.class, id);
    }
    
    @Override
    public List<Payment> findAll() {
        TypedQuery<Payment> query = em.createQuery("SELECT p FROM Payment p", Payment.class);
        return query.getResultList();
    }
    
    @Override
    public List<Payment> findByClientId(Long clientId) {
        TypedQuery<Payment> query = em.createQuery(
            "SELECT p FROM Payment p WHERE p.client.id = :clientId", 
            Payment.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
    
    @Override
    public List<Payment> findByServiceId(Long serviceId) {
        TypedQuery<Payment> query = em.createQuery(
            "SELECT p FROM Payment p WHERE p.service.id = :serviceId", 
            Payment.class);
        query.setParameter("serviceId", serviceId);
        return query.getResultList();
    }
    
    @Override
    public void save(Payment payment) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (payment.getId() == null) {
                em.persist(payment);
            } else {
                payment = em.merge(payment);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении покупки", e);
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
            
            Payment payment = findById(id);
            if (payment != null) {
                em.remove(payment);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении покупки", e);
        }
    }
}



