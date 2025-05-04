package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.repository.CurriculumRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurriculumServiceTest {

    @Mock
    private CurriculumRepository curriculumRepository;

    @InjectMocks
    private CurriculumService curriculumService;

    private Curriculum curriculum1;
    private Curriculum curriculum2;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные
        curriculum1 = new Curriculum();
        curriculum1.setId(1L);
        curriculum1.setAcademicYear("2023/2024");
        curriculum1.setSpeciality("09.03.04");
        curriculum1.setQualification("Бакалавр");
        curriculum1.setFormEducation("Очная");
        curriculum1.setNameCurriculum("Программная инженерия");
        curriculum1.setCourse(1);

        curriculum2 = new Curriculum();
        curriculum2.setId(2L);
        curriculum2.setAcademicYear("2023/2024");
        curriculum2.setSpeciality("09.03.01");
        curriculum2.setQualification("Бакалавр");
        curriculum2.setFormEducation("Очная");
        curriculum2.setNameCurriculum("Информатика и вычислительная техника");
        curriculum2.setCourse(2);
    }

    @Test
    void testGetAllCurriculums() {
        // Подготовка
        List<Curriculum> curriculums = Arrays.asList(curriculum1, curriculum2);
        when(curriculumRepository.findAll()).thenReturn(curriculums);

        // Выполнение
        List<Curriculum> result = curriculumService.getAllCurriculums();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Программная инженерия", result.get(0).getNameCurriculum());
        assertEquals("Информатика и вычислительная техника", result.get(1).getNameCurriculum());
        verify(curriculumRepository, times(1)).findAll();
    }

    @Test
    void testGetCurriculumById() {
        // Подготовка
        when(curriculumRepository.findById(1L)).thenReturn(curriculum1);

        // Выполнение
        Curriculum result = curriculumService.getCurriculumById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Программная инженерия", result.getNameCurriculum());
        assertEquals(1, result.getCourse());
        verify(curriculumRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchCurriculumsByName() {
        // Подготовка
        String searchName = "программ";
        when(curriculumRepository.findByName(searchName)).thenReturn(Arrays.asList(curriculum1));

        // Выполнение
        List<Curriculum> result = curriculumService.searchCurriculumsByName(searchName);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Программная инженерия", result.get(0).getNameCurriculum());
        verify(curriculumRepository, times(1)).findByName(searchName);
    }

    @Test
    void testGetCurriculumsByCourse() {
        // Подготовка
        when(curriculumRepository.findByCourse(1)).thenReturn(Arrays.asList(curriculum1));

        // Выполнение
        List<Curriculum> result = curriculumService.getCurriculumsByCourse(1);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Программная инженерия", result.get(0).getNameCurriculum());
        verify(curriculumRepository, times(1)).findByCourse(1);
    }

    @Test
    void testGetCurriculumsBySpeciality() {
        // Подготовка
        String speciality = "09.03.04";
        when(curriculumRepository.findBySpeciality(speciality)).thenReturn(Arrays.asList(curriculum1));

        // Выполнение
        List<Curriculum> result = curriculumService.getCurriculumsBySpeciality(speciality);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Программная инженерия", result.get(0).getNameCurriculum());
        verify(curriculumRepository, times(1)).findBySpeciality(speciality);
    }

    @Test
    void testSaveCurriculum() {
        // Выполнение
        curriculumService.saveCurriculum(curriculum1);

        // Проверка
        verify(curriculumRepository, times(1)).save(curriculum1);
    }

    @Test
    void testDeleteCurriculum() {
        // Выполнение
        curriculumService.deleteCurriculum(1L);

        // Проверка
        verify(curriculumRepository, times(1)).delete(1L);
    }
} 