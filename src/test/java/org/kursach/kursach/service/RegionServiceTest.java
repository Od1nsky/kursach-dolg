package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.repository.RegionRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    private Region central;
    private Region volga;

    @BeforeEach
    void setUp() {
        central = new Region("Центральный округ", "CR-01");
        central.setId(1L);
        volga = new Region("Приволжский округ", "VG-02");
        volga.setId(2L);
    }

    @Test
    void getAllRegionsReturnsRepositoryData() {
        when(regionRepository.findAll()).thenReturn(Arrays.asList(central, volga));

        List<Region> regions = regionService.getAllRegions();

        assertEquals(2, regions.size());
        verify(regionRepository).findAll();
    }

    @Test
    void searchRegionsDelegatesToRepository() {
        when(regionRepository.searchByTerm("Центр")).thenReturn(List.of(central));

        List<Region> result = regionService.searchRegions("Центр");

        assertEquals(1, result.size());
        assertEquals("Центральный округ", result.get(0).getName());
        verify(regionRepository).searchByTerm("Центр");
    }

    @Test
    void saveRegionPersistsEntity() {
        regionService.saveRegion(central);

        verify(regionRepository).save(central);
    }

    @Test
    void getRegionReturnsEntity() {
        when(regionRepository.findById(1L)).thenReturn(central);

        Region found = regionService.getRegion(1L);

        assertNotNull(found);
        assertEquals("Центральный округ", found.getName());
        verify(regionRepository).findById(1L);
    }

    @Test
    void deleteRegionDelegatesToRepository() {
        regionService.deleteRegion(2L);

        verify(regionRepository).delete(2L);
    }
}

