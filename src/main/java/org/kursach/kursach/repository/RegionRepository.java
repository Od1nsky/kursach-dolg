package org.kursach.kursach.repository;

import org.kursach.kursach.model.Region;

import java.util.List;

public interface RegionRepository extends Repository<Region, Long> {

    List<Region> searchByTerm(String term);
}

