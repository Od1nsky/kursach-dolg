package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.repository.FacultyRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    private Faculty faculty1;
    private Faculty faculty2;

    @BeforeEach
    void setUp() {
        faculty1 = new Faculty("Факультет информатики", "ФИ");
        faculty1.setId(1L);
        
        faculty2 = new Faculty("Факультет экономики", "ФЭ");
        faculty2.setId(2L);
    }

    @Test
    void testGetAllFaculties() {
        // Подготовка
        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);
        when(facultyRepository.findAll()).thenReturn(faculties);

        // Выполнение
        List<Faculty> result = facultyService.getAllFaculties();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Факультет информатики", result.get(0).getNameFaculty());
        assertEquals("Факультет экономики", result.get(1).getNameFaculty());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void testSearchFacultiesByName() {
        // Подготовка
        String searchName = "информатики";
        when(facultyRepository.findByName(searchName)).thenReturn(Arrays.asList(faculty1));

        // Выполнение
        List<Faculty> result = facultyService.searchFacultiesByName(searchName);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Факультет информатики", result.get(0).getNameFaculty());
        verify(facultyRepository, times(1)).findByName(searchName);
    }

    @Test
    void testGetFacultyById() {
        // Подготовка
        when(facultyRepository.findById(1L)).thenReturn(faculty1);

        // Выполнение
        Faculty result = facultyService.getFacultyById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Факультет информатики", result.getNameFaculty());
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveFaculty() {
        // Выполнение
        facultyService.saveFaculty(faculty1);

        // Проверка
        verify(facultyRepository, times(1)).save(faculty1);
    }

    @Test
    void testDeleteFaculty() {
        // Выполнение
        facultyService.deleteFaculty(1L);

        // Проверка
        verify(facultyRepository, times(1)).delete(1L);
    }
} 