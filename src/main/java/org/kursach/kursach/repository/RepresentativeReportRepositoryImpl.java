package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.RepresentativeReport;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RepresentativeReportRepositoryImpl implements RepresentativeReportRepository {

    public RepresentativeReportRepositoryImpl() {
    }

    @Inject
    private EntityManager em;

    @Override
    public RepresentativeReport findById(Long id) {
        return em.find(RepresentativeReport.class, id);
    }

    @Override
    public List<RepresentativeReport> findAll() {
        TypedQuery<RepresentativeReport> query = em.createQuery(
                "SELECT rr FROM RepresentativeReport rr ORDER BY rr.periodStart DESC",
                RepresentativeReport.class
        );
        return query.getResultList();
    }

    @Override
    public List<RepresentativeReport> findByRepresentative(Long representativeId) {
        if (representativeId == null) {
            return findAll();
        }

        TypedQuery<RepresentativeReport> query = em.createQuery(
                "SELECT rr FROM RepresentativeReport rr WHERE rr.representative.id = :repId ORDER BY rr.periodStart DESC",
                RepresentativeReport.class
        );
        query.setParameter("repId", representativeId);
        return query.getResultList();
    }

    @Override
    public List<RepresentativeReport> findByPeriod(LocalDate from, LocalDate to) {
        StringBuilder jpql = new StringBuilder("SELECT rr FROM RepresentativeReport rr WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (from != null) {
            jpql.append(" AND rr.periodStart >= :fromDate");
            params.put("fromDate", from);
        }

        if (to != null) {
            jpql.append(" AND rr.periodEnd <= :toDate");
            params.put("toDate", to);
        }

        jpql.append(" ORDER BY rr.periodStart DESC");
        TypedQuery<RepresentativeReport> query = em.createQuery(jpql.toString(), RepresentativeReport.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public void save(RepresentativeReport report) {
        boolean transactionStarted = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionStarted = true;
            }

            if (report.getId() == null) {
                em.persist(report);
            } else {
                em.merge(report);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось сохранить отчет", e);
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

            RepresentativeReport report = findById(id);
            if (report != null) {
                em.remove(report);
            }

            if (transactionStarted) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Не удалось удалить отчет", e);
        }
    }
}

