package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.BranchOffice;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.service.BranchOfficeService;
import org.kursach.kursach.service.RegionService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class BranchOfficeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(BranchOfficeController.class.getName());

    @Inject
    private BranchOfficeService branchOfficeService;

    @Inject
    private RegionService regionService;

    private List<BranchOffice> offices;
    private List<Region> regions;
    private BranchOffice office;
    private Long editId;
    private String searchTerm;
    private Long filterRegionId;
    private Long selectedRegionId;

    @PostConstruct
    public void init() {
        regions = regionService.getAllRegions();
        offices = branchOfficeService.getAllOffices();
        if (office == null) {
            office = new BranchOffice();
        }
        loadOffice();
    }

    public void loadOffice() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam == null || idParam.isBlank()) {
            return;
        }

        try {
            editId = Long.valueOf(idParam);
            office = branchOfficeService.getOffice(editId);
            if (office != null && office.getRegion() != null) {
                selectedRegionId = office.getRegion().getId();
            }
        } catch (NumberFormatException e) {
            logger.warning("Некорректный ID офиса: " + idParam);
        }
    }

    public String save() {
        try {
            if (selectedRegionId != null) {
                Region region = regionService.getRegion(selectedRegionId);
                office.setRegion(region);
            } else {
                office.setRegion(null);
            }

            if (editId != null && office.getId() == null) {
                office.setId(editId);
            }

            branchOfficeService.saveOffice(office);
            offices = branchOfficeService.getAllOffices();
            office = new BranchOffice();
            selectedRegionId = null;
            editId = null;
            return "office?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении офиса: " + e.getMessage());
            return null;
        }
    }

    public String edit(Long id) {
        BranchOffice found = branchOfficeService.getOffice(id);
        if (found == null) {
            return "office?faces-redirect=true";
        }
        this.office = found;
        this.editId = id;
        this.selectedRegionId = found.getRegion() != null ? found.getRegion().getId() : null;
        return "office-edit?faces-redirect=true&id=" + id;
    }

    public String delete(Long id) {
        branchOfficeService.deleteOffice(id);
        offices = branchOfficeService.searchOffices(searchTerm, filterRegionId);
        return "office?faces-redirect=true";
    }

    public void search() {
        offices = branchOfficeService.searchOffices(searchTerm, filterRegionId);
    }

    public void resetSearch() {
        searchTerm = null;
        filterRegionId = null;
        offices = branchOfficeService.getAllOffices();
    }

    public String prepareNew() {
        office = new BranchOffice();
        selectedRegionId = null;
        editId = null;
        return "office-edit?faces-redirect=true";
    }

    public List<BranchOffice> getOffices() {
        return offices;
    }

    public void setOffices(List<BranchOffice> offices) {
        this.offices = offices;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public BranchOffice getOffice() {
        return office;
    }

    public void setOffice(BranchOffice office) {
        this.office = office;
    }

    public Long getEditId() {
        return editId;
    }

    public void setEditId(Long editId) {
        this.editId = editId;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Long getFilterRegionId() {
        return filterRegionId;
    }

    public void setFilterRegionId(Long filterRegionId) {
        this.filterRegionId = filterRegionId;
    }

    public Long getSelectedRegionId() {
        return selectedRegionId;
    }

    public void setSelectedRegionId(Long selectedRegionId) {
        this.selectedRegionId = selectedRegionId;
    }
}

