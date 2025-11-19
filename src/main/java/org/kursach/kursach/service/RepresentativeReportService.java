package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.RepresentativeReport;
import org.kursach.kursach.repository.RepresentativeReportRepository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RepresentativeReportService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RepresentativeReportRepository reportRepository;

    public List<RepresentativeReport> getAllReports() {
        return reportRepository.findAll();
    }

    public List<RepresentativeReport> getReportsByRepresentative(Long representativeId) {
        return reportRepository.findByRepresentative(representativeId);
    }

    public List<RepresentativeReport> findReportsByPeriod(LocalDate from, LocalDate to) {
        return reportRepository.findByPeriod(from, to);
    }

    public RepresentativeReport getReport(Long id) {
        return reportRepository.findById(id);
    }

    public void saveReport(RepresentativeReport report) {
        reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.delete(id);
    }
}

