package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.BranchOffice;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeStatus;
import org.kursach.kursach.service.BranchOfficeService;
import org.kursach.kursach.service.RegionService;
import org.kursach.kursach.service.RepresentativeService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class RepresentativeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RepresentativeController.class.getName());

    @Inject
    private RepresentativeService representativeService;

    @Inject
    private RegionService regionService;

    @Inject
    private BranchOfficeService branchOfficeService;

    private List<Representative> representatives;
    private List<Region> regions;
    private List<BranchOffice> officeOptions;
    private List<BranchOffice> allOffices;
    private Representative representative;
    private Long editId;

    private String searchTerm;
    private RepresentativeStatus filterStatus;
    private Long filterRegionId;
    private Long filterOfficeId;

    private Long selectedRegionId;
    private Long selectedOfficeId;

    @PostConstruct
    public void init() {
        regions = regionService.getAllRegions();
        allOffices = branchOfficeService.getAllOffices();
        officeOptions = allOffices;
        representatives = representativeService.getAllRepresentatives();
        representative = new Representative();
        loadRepresentative();
    }

    public void loadRepresentative() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam == null || idParam.isBlank()) {
            return;
        }

        try {
            editId = Long.valueOf(idParam);
            representative = representativeService.getRepresentative(editId);
            if (representative != null) {
                selectedRegionId = representative.getRegion() != null ? representative.getRegion().getId() : null;
                selectedOfficeId = representative.getBranchOffice() != null ? representative.getBranchOffice().getId() : null;
                updateOfficeOptions();
            } else {
                representative = new Representative();
                officeOptions = allOffices;
            }
        } catch (NumberFormatException e) {
            logger.warning("Некорректный ID представителя: " + idParam);
        }
    }

    public String save() {
        try {
            if (selectedRegionId != null) {
                Region region = regionService.getRegion(selectedRegionId);
                representative.setRegion(region);
            } else {
                representative.setRegion(null);
            }

            if (selectedOfficeId != null) {
                BranchOffice office = branchOfficeService.getOffice(selectedOfficeId);
                representative.setBranchOffice(office);
            } else {
                representative.setBranchOffice(null);
            }

            if (representative.getStatus() == null) {
                representative.setStatus(RepresentativeStatus.ACTIVE);
            }

            if (editId != null && representative.getId() == null) {
                representative.setId(editId);
            }

            representativeService.saveRepresentative(representative);
            representatives = representativeService.getAllRepresentatives();
            representative = new Representative();
            editId = null;
            selectedRegionId = null;
            selectedOfficeId = null;
            return "representative?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении представителя: " + e.getMessage());
            return null;
        }
    }

    public String edit(Long id) {
        Representative found = representativeService.getRepresentative(id);
        if (found == null) {
            return "representative?faces-redirect=true";
        }
        representative = found;
        editId = id;
        selectedRegionId = found.getRegion() != null ? found.getRegion().getId() : null;
        selectedOfficeId = found.getBranchOffice() != null ? found.getBranchOffice().getId() : null;
        updateOfficeOptions();
        return "representative-edit?faces-redirect=true&id=" + id;
    }

    public String delete(Long id) {
        representativeService.deleteRepresentative(id);
        representatives = representativeService.searchRepresentatives(searchTerm, filterStatus, filterRegionId, filterOfficeId);
        return "representative?faces-redirect=true";
    }

    public void search() {
        representatives = representativeService.searchRepresentatives(searchTerm, filterStatus, filterRegionId, filterOfficeId);
    }

    public void resetSearch() {
        searchTerm = null;
        filterStatus = null;
        filterRegionId = null;
        filterOfficeId = null;
        representatives = representativeService.getAllRepresentatives();
    }

    public void updateOfficeOptions() {
        if (selectedRegionId != null) {
            officeOptions = branchOfficeService.getOfficesByRegion(selectedRegionId);
        } else {
            officeOptions = branchOfficeService.getAllOffices();
        }
    }

    public String prepareNew() {
        representative = new Representative();
        editId = null;
        selectedRegionId = null;
        selectedOfficeId = null;
        updateOfficeOptions();
        return "representative-edit?faces-redirect=true";
    }

    public List<Representative> getRepresentatives() {
        return representatives;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
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

    public RepresentativeStatus getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(RepresentativeStatus filterStatus) {
        this.filterStatus = filterStatus;
    }

    public Long getFilterRegionId() {
        return filterRegionId;
    }

    public void setFilterRegionId(Long filterRegionId) {
        this.filterRegionId = filterRegionId;
    }

    public Long getFilterOfficeId() {
        return filterOfficeId;
    }

    public void setFilterOfficeId(Long filterOfficeId) {
        this.filterOfficeId = filterOfficeId;
    }

    public Long getSelectedRegionId() {
        return selectedRegionId;
    }

    public void setSelectedRegionId(Long selectedRegionId) {
        this.selectedRegionId = selectedRegionId;
    }

    public Long getSelectedOfficeId() {
        return selectedOfficeId;
    }

    public void setSelectedOfficeId(Long selectedOfficeId) {
        this.selectedOfficeId = selectedOfficeId;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public List<BranchOffice> getOfficeOptions() {
        return officeOptions;
    }

    public List<BranchOffice> getAllOffices() {
        return allOffices;
    }

    public List<RepresentativeStatus> getStatuses() {
        return representativeService.getAvailableStatuses();
    }
}

