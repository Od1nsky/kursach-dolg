package org.kursach.kursach.repository;

import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeStatus;

import java.util.List;

public interface RepresentativeRepository extends Repository<Representative, Long> {

    List<Representative> findByRegion(Long regionId);

    List<Representative> search(String term,
                                RepresentativeStatus status,
                                Long regionId,
                                Long branchOfficeId);
}

