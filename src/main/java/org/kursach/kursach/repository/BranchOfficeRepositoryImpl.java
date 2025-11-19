package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.BranchOffice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class BranchOfficeRepositoryImpl implements BranchOfficeRepository {

    public BranchOfficeRepositoryImpl() {
    }

    @Inject
    private EntityManager em;

    @Override
    public BranchOffice findById(Long id) {
        return em.find(BranchOffice.class, id);
    }

    @Override
    public List<BranchOffice> findAll() {
        TypedQuery<BranchOffice> query = em.createQuery(
                "SELECT b FROM BranchOffice b ORDER BY b.city, b.title",
                BranchOffice.class
        );
        return query.getResultList();
    }

    @Override
    public List<BranchOffice> findByRegion(Long regionId) {
        if (regionId == null) {
            return findAll();
        }

        TypedQuery<BranchOffice> query = em.createQuery(
                "SELECT b FROM BranchOffice b WHERE b.region.id = :regionId ORDER BY b.title",
                BranchOffice.class
        );
        query.setParameter("regionId", regionId);
        return query.getResultList();
    }

    @Override
    public List<BranchOffice> search(String term, Long regionId) {
        StringBuilder jpql = new StringBuilder("SELECT b FROM BranchOffice b WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (term != null && !term.trim().isEmpty()) {
            jpql.append(" AND (LOWER(b.title) LIKE :term OR LOWER(b.city) LIKE :term OR LOWER(b.managerName) LIKE :term)");
            params.put("term", "%" + term.toLowerCase() + "%");
        }

        if (regionId != null) {
            jpql.append(" AND b.region.id = :regionId");
            params.put("regionId", regionId);
        }

        jpql.append(" ORDER BY b.city, b.title");
        TypedQuery<BranchOffice> query = em.createQuery(jpql.toString(), BranchOffice.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public void save(BranchOffice office) {
        boolean transactionStarted = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionStarted = true;
            }

            if (office.getId() == null) {
                em.persist(office);
            } else {
                em.merge(office);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось сохранить региональный офис", e);
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

            BranchOffice office = findById(id);
            if (office != null) {
                em.remove(office);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось удалить региональный офис", e);
        }
    }
}

