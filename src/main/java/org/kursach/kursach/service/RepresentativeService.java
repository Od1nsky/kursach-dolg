package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeStatus;
import org.kursach.kursach.repository.RepresentativeRepository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class RepresentativeService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RepresentativeRepository representativeRepository;

    public List<Representative> getAllRepresentatives() {
        return representativeRepository.findAll();
    }

    public List<Representative> searchRepresentatives(String term,
                                                      RepresentativeStatus status,
                                                      Long regionId,
                                                      Long branchOfficeId) {
        return representativeRepository.search(term, status, regionId, branchOfficeId);
    }

    public Representative getRepresentative(Long id) {
        return representativeRepository.findById(id);
    }

    public void saveRepresentative(Representative representative) {
        representativeRepository.save(representative);
    }

    public void deleteRepresentative(Long id) {
        representativeRepository.delete(id);
    }

    public List<RepresentativeStatus> getAvailableStatuses() {
        return Arrays.asList(RepresentativeStatus.values());
    }
}

