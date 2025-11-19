package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeReport;
import org.kursach.kursach.repository.RepresentativeReportRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepresentativeReportServiceTest {

    @Mock
    private RepresentativeReportRepository reportRepository;

    @InjectMocks
    private RepresentativeReportService reportService;

    private Representative representative;
    private RepresentativeReport report;

    @BeforeEach
    void setUp() {
        representative = new Representative();
        representative.setId(8L);

        report = new RepresentativeReport();
        report.setId(20L);
        report.setRepresentative(representative);
        report.setPeriodStart(LocalDate.of(2025, 1, 1));
        report.setPeriodEnd(LocalDate.of(2025, 1, 31));
    }

    @Test
    void getReportsByRepresentativeDelegatesToRepository() {
        when(reportRepository.findByRepresentative(8L)).thenReturn(List.of(report));

        List<RepresentativeReport> reports = reportService.getReportsByRepresentative(8L);

        assertEquals(1, reports.size());
        verify(reportRepository).findByRepresentative(8L);
    }

    @Test
    void findReportsByPeriodUsesRepositoryFilter() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 31);
        when(reportRepository.findByPeriod(from, to)).thenReturn(List.of(report));

        List<RepresentativeReport> reports = reportService.findReportsByPeriod(from, to);

        assertEquals(1, reports.size());
        verify(reportRepository).findByPeriod(from, to);
    }

    @Test
    void saveReportCallsRepository() {
        reportService.saveReport(report);

        verify(reportRepository).save(report);
    }

    @Test
    void deleteReportDelegatesToRepository() {
        reportService.deleteReport(20L);

        verify(reportRepository).delete(20L);
    }
}

