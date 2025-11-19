package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeReport;
import org.kursach.kursach.service.RepresentativeReportService;
import org.kursach.kursach.service.RepresentativeService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class RepresentativeReportController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RepresentativeReportController.class.getName());

    @Inject
    private RepresentativeReportService reportService;

    @Inject
    private RepresentativeService representativeService;

    private List<RepresentativeReport> reports;
    private List<Representative> representatives;
    private RepresentativeReport report;
    private Long editId;

    private Long selectedRepresentativeId;
    private Long filterRepresentativeId;
    private LocalDate filterFrom;
    private LocalDate filterTo;

    @PostConstruct
    public void init() {
        representatives = representativeService.getAllRepresentatives();
        reports = reportService.getAllReports();
        report = new RepresentativeReport();
        loadReport();
    }

    public void loadReport() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam == null || idParam.isBlank()) {
            return;
        }

        try {
            editId = Long.valueOf(idParam);
            report = reportService.getReport(editId);
            if (report != null && report.getRepresentative() != null) {
                selectedRepresentativeId = report.getRepresentative().getId();
            }
        } catch (NumberFormatException e) {
            logger.warning("Некорректный ID отчета: " + idParam);
        }
    }

    public String save() {
        try {
            if (selectedRepresentativeId != null) {
                Representative representative = representativeService.getRepresentative(selectedRepresentativeId);
                report.setRepresentative(representative);
            } else {
                report.setRepresentative(null);
            }

            if (editId != null && report.getId() == null) {
                report.setId(editId);
            }

            reportService.saveReport(report);
            reports = reportService.getAllReports();
            report = new RepresentativeReport();
            selectedRepresentativeId = null;
            editId = null;
            return "report?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении отчета: " + e.getMessage());
            return null;
        }
    }

    public String edit(Long id) {
        RepresentativeReport found = reportService.getReport(id);
        if (found == null) {
            return "report?faces-redirect=true";
        }
        report = found;
        editId = id;
        selectedRepresentativeId = found.getRepresentative() != null ? found.getRepresentative().getId() : null;
        return "report-edit?faces-redirect=true&id=" + id;
    }

    public String delete(Long id) {
        reportService.deleteReport(id);
        search();
        return "report?faces-redirect=true";
    }

    public void search() {
        reports = reportService.findReportsByPeriod(filterFrom, filterTo);
        if (filterRepresentativeId != null) {
            reports = reports.stream()
                    .filter(r -> r.getRepresentative() != null && filterRepresentativeId.equals(r.getRepresentative().getId()))
                    .collect(Collectors.toList());
        }
    }

    public void resetSearch() {
        filterFrom = null;
        filterTo = null;
        filterRepresentativeId = null;
        reports = reportService.getAllReports();
    }

    public String prepareNew() {
        report = new RepresentativeReport();
        selectedRepresentativeId = null;
        editId = null;
        return "report-edit?faces-redirect=true";
    }

    public List<RepresentativeReport> getReports() {
        return reports;
    }

    public RepresentativeReport getReport() {
        return report;
    }

    public void setReport(RepresentativeReport report) {
        this.report = report;
    }

    public Long getEditId() {
        return editId;
    }

    public void setEditId(Long editId) {
        this.editId = editId;
    }

    public Long getSelectedRepresentativeId() {
        return selectedRepresentativeId;
    }

    public void setSelectedRepresentativeId(Long selectedRepresentativeId) {
        this.selectedRepresentativeId = selectedRepresentativeId;
    }

    public Long getFilterRepresentativeId() {
        return filterRepresentativeId;
    }

    public void setFilterRepresentativeId(Long filterRepresentativeId) {
        this.filterRepresentativeId = filterRepresentativeId;
    }

    public LocalDate getFilterFrom() {
        return filterFrom;
    }

    public void setFilterFrom(LocalDate filterFrom) {
        this.filterFrom = filterFrom;
    }

    public LocalDate getFilterTo() {
        return filterTo;
    }

    public void setFilterTo(LocalDate filterTo) {
        this.filterTo = filterTo;
    }

    public List<Representative> getRepresentatives() {
        return representatives;
    }
}

