package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.BranchOffice;
import org.kursach.kursach.repository.BranchOfficeRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class BranchOfficeService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private BranchOfficeRepository branchOfficeRepository;

    public List<BranchOffice> getAllOffices() {
        return branchOfficeRepository.findAll();
    }

    public List<BranchOffice> getOfficesByRegion(Long regionId) {
        return branchOfficeRepository.findByRegion(regionId);
    }

    public List<BranchOffice> searchOffices(String term, Long regionId) {
        return branchOfficeRepository.search(term, regionId);
    }

    public BranchOffice getOffice(Long id) {
        return branchOfficeRepository.findById(id);
    }

    public void saveOffice(BranchOffice office) {
        branchOfficeRepository.save(office);
    }

    public void deleteOffice(Long id) {
        branchOfficeRepository.delete(id);
    }
}

