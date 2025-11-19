package org.kursach.kursach.repository;

import org.kursach.kursach.model.BranchOffice;

import java.util.List;

public interface BranchOfficeRepository extends Repository<BranchOffice, Long> {

    List<BranchOffice> findByRegion(Long regionId);

    List<BranchOffice> search(String term, Long regionId);
}

