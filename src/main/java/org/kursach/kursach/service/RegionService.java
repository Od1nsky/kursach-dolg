package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.repository.RegionRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class RegionService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RegionRepository regionRepository;

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public List<Region> searchRegions(String term) {
        return regionRepository.searchByTerm(term);
    }

    public Region getRegion(Long id) {
        return regionRepository.findById(id);
    }

    public void saveRegion(Region region) {
        regionRepository.save(region);
    }

    public void deleteRegion(Long id) {
        regionRepository.delete(id);
    }
}

