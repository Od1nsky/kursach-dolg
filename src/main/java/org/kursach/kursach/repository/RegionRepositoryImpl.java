package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Region;

import java.util.List;

@ApplicationScoped
public class RegionRepositoryImpl implements RegionRepository {

    public RegionRepositoryImpl() {
    }

    @Inject
    private EntityManager em;

    @Override
    public Region findById(Long id) {
        return em.find(Region.class, id);
    }

    @Override
    public List<Region> findAll() {
        TypedQuery<Region> query = em.createQuery(
                "SELECT r FROM Region r ORDER BY r.name",
                Region.class
        );
        return query.getResultList();
    }

    @Override
    public List<Region> searchByTerm(String term) {
        if (term == null || term.trim().isEmpty()) {
            return findAll();
        }

        String pattern = "%" + term.toLowerCase() + "%";
        TypedQuery<Region> query = em.createQuery(
                "SELECT r FROM Region r " +
                        "WHERE LOWER(r.name) LIKE :term " +
                        "OR LOWER(r.regionCode) LIKE :term " +
                        "OR LOWER(r.curatorName) LIKE :term",
                Region.class
        );
        query.setParameter("term", pattern);
        return query.getResultList();
    }

    @Override
    public void save(Region region) {
        boolean transactionStarted = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionStarted = true;
            }

            if (region.getId() == null) {
                em.persist(region);
            } else {
                em.merge(region);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось сохранить регион", e);
        }
    }

    @Override
    public void delete(Long id) {
        boolean transactionStarted = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionStarted = true;
            }

            Region region = findById(id);
            if (region != null) {
                em.remove(region);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось удалить регион", e);
        }
    }
}

