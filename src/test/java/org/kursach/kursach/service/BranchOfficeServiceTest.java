package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.BranchOffice;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.repository.BranchOfficeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchOfficeServiceTest {

    @Mock
    private BranchOfficeRepository branchOfficeRepository;

    @InjectMocks
    private BranchOfficeService branchOfficeService;

    private Region region;
    private BranchOffice office;

    @BeforeEach
    void setUp() {
        region = new Region("Сибирь", "SB-10");
        region.setId(10L);

        office = new BranchOffice("Новосибирск", "Новосибирск", region);
        office.setId(5L);
    }

    @Test
    void getAllOfficesReturnsRepositoryData() {
        when(branchOfficeRepository.findAll()).thenReturn(List.of(office));

        List<BranchOffice> offices = branchOfficeService.getAllOffices();

        assertEquals(1, offices.size());
        verify(branchOfficeRepository).findAll();
    }

    @Test
    void getOfficesByRegionDelegatesToRepository() {
        when(branchOfficeRepository.findByRegion(10L)).thenReturn(List.of(office));

        List<BranchOffice> offices = branchOfficeService.getOfficesByRegion(10L);

        assertEquals(1, offices.size());
        verify(branchOfficeRepository).findByRegion(10L);
    }

    @Test
    void searchOfficesUsesRepositoryFilter() {
        when(branchOfficeRepository.search("Новос", 10L)).thenReturn(List.of(office));

        List<BranchOffice> offices = branchOfficeService.searchOffices("Новос", 10L);

        assertEquals(1, offices.size());
        verify(branchOfficeRepository).search("Новос", 10L);
    }

    @Test
    void saveOfficeCallsRepository() {
        branchOfficeService.saveOffice(office);

        verify(branchOfficeRepository).save(office);
    }

    @Test
    void deleteOfficeDelegatesToRepository() {
        branchOfficeService.deleteOffice(5L);

        verify(branchOfficeRepository).delete(5L);
    }
}

