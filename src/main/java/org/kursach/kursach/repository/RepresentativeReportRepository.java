package org.kursach.kursach.repository;

import org.kursach.kursach.model.RepresentativeReport;

import java.time.LocalDate;
import java.util.List;

public interface RepresentativeReportRepository extends Repository<RepresentativeReport, Long> {

    List<RepresentativeReport> findByRepresentative(Long representativeId);

    List<RepresentativeReport> findByPeriod(LocalDate from, LocalDate to);
}

