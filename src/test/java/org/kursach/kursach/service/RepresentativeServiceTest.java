package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.BranchOffice;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.model.RepresentativeStatus;
import org.kursach.kursach.repository.RepresentativeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepresentativeServiceTest {

    @Mock
    private RepresentativeRepository representativeRepository;

    @InjectMocks
    private RepresentativeService representativeService;

    private Representative representative;
    private Region region;
    private BranchOffice office;

    @BeforeEach
    void setUp() {
        region = new Region("Юг", "YG-03");
        region.setId(3L);

        office = new BranchOffice("Краснодар", "Краснодар", region);
        office.setId(7L);

        representative = new Representative("Игорь Волков", region, office);
        representative.setId(15L);
        representative.setStatus(RepresentativeStatus.ACTIVE);
    }

    @Test
    void searchRepresentativesDelegatesToRepository() {
        when(representativeRepository.search("Игорь", RepresentativeStatus.ACTIVE, 3L, 7L))
                .thenReturn(List.of(representative));

        List<Representative> result = representativeService.searchRepresentatives("Игорь", RepresentativeStatus.ACTIVE, 3L, 7L);

        assertEquals(1, result.size());
        verify(representativeRepository).search("Игорь", RepresentativeStatus.ACTIVE, 3L, 7L);
    }

    @Test
    void getRepresentativeReturnsEntity() {
        when(representativeRepository.findById(15L)).thenReturn(representative);

        Representative found = representativeService.getRepresentative(15L);

        assertNotNull(found);
        assertEquals("Игорь Волков", found.getFullName());
        verify(representativeRepository).findById(15L);
    }

    @Test
    void saveRepresentativeCallsRepository() {
        representativeService.saveRepresentative(representative);

        verify(representativeRepository).save(representative);
    }

    @Test
    void deleteRepresentativeDelegatesToRepository() {
        representativeService.deleteRepresentative(15L);

        verify(representativeRepository).delete(15L);
    }

    @Test
    void getAvailableStatusesReturnsAllEnumValues() {
        List<RepresentativeStatus> statuses = representativeService.getAvailableStatuses();

        assertEquals(RepresentativeStatus.values().length, statuses.size());
        assertTrue(statuses.contains(RepresentativeStatus.ACTIVE));
    }
}

