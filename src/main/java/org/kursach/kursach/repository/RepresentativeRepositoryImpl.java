package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RepresentativeRepositoryImpl implements RepresentativeRepository {

    public RepresentativeRepositoryImpl() {
    }

    @Inject
    private EntityManager em;

    @Override
    public Representative findById(Long id) {
        return em.find(Representative.class, id);
    }

    @Override
    public List<Representative> findAll() {
        TypedQuery<Representative> query = em.createQuery(
                "SELECT r FROM Representative r ORDER BY r.fullName",
                Representative.class
        );
        return query.getResultList();
    }

    @Override
    public List<Representative> findByRegion(Long regionId) {
        if (regionId == null) {
            return findAll();
        }

        TypedQuery<Representative> query = em.createQuery(
                "SELECT r FROM Representative r WHERE r.region.id = :regionId ORDER BY r.fullName",
                Representative.class
        );
        query.setParameter("regionId", regionId);
        return query.getResultList();
    }

    @Override
    public List<Representative> search(String term,
                                       RepresentativeStatus status,
                                       Long regionId,
                                       Long branchOfficeId) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT r FROM Representative r WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (term != null && !term.trim().isEmpty()) {
            jpql.append(" AND (LOWER(r.fullName) LIKE :term OR LOWER(r.positionTitle) LIKE :term OR LOWER(r.specialization) LIKE :term)");
            params.put("term", "%" + term.toLowerCase() + "%");
        }

        if (status != null) {
            jpql.append(" AND r.status = :status");
            params.put("status", status);
        }

        if (regionId != null) {
            jpql.append(" AND r.region.id = :regionId");
            params.put("regionId", regionId);
        }

        if (branchOfficeId != null) {
            jpql.append(" AND r.branchOffice.id = :branchOfficeId");
            params.put("branchOfficeId", branchOfficeId);
        }

        jpql.append(" ORDER BY r.fullName");
        TypedQuery<Representative> query = em.createQuery(jpql.toString(), Representative.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public void save(Representative representative) {
        boolean transactionStarted = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionStarted = true;
            }

            if (representative.getId() == null) {
                em.persist(representative);
            } else {
                em.merge(representative);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось сохранить представителя", e);
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

            Representative representative = findById(id);
            if (representative != null) {
                em.remove(representative);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось удалить представителя", e);
        }
    }
}

