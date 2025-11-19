package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.service.RegionService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class RegionController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RegionController.class.getName());

    @Inject
    private RegionService regionService;

    private List<Region> regions;
    private Region region;
    private Long editId;
    private String searchTerm;

    @PostConstruct
    public void init() {
        regions = regionService.getAllRegions();
        if (region == null) {
            region = new Region();
        }
        loadRegion();
    }

    public void loadRegion() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam == null || idParam.isBlank()) {
            return;
        }

        try {
            editId = Long.valueOf(idParam);
            region = regionService.getRegion(editId);
            if (region == null) {
                region = new Region();
                logger.warning("Регион с ID " + editId + " не найден");
            }
        } catch (NumberFormatException e) {
            logger.warning("Некорректный ID региона: " + idParam);
        }
    }

    public String save() {
        try {
            if (editId != null && region.getId() == null) {
                region.setId(editId);
            }
            regionService.saveRegion(region);
            regions = regionService.getAllRegions();
            region = new Region();
            editId = null;
            return "region?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении региона: " + e.getMessage());
            return null;
        }
    }

    public String edit(Long id) {
        Region found = regionService.getRegion(id);
        if (found == null) {
            logger.warning("Регион с ID " + id + " не найден для редактирования");
            return "region?faces-redirect=true";
        }
        this.region = found;
        this.editId = id;
        return "region-edit?faces-redirect=true&id=" + id;
    }

    public String delete(Long id) {
        try {
            regionService.deleteRegion(id);
            regions = regionService.getAllRegions();
            return "region?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Не удалось удалить регион: " + e.getMessage());
            return null;
        }
    }

    public void search() {
        regions = regionService.searchRegions(searchTerm);
    }

    public void resetSearch() {
        searchTerm = null;
        regions = regionService.getAllRegions();
    }

    public String prepareNew() {
        region = new Region();
        editId = null;
        return "region-edit?faces-redirect=true";
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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
}

