package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.repository.ChairRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChairServiceTest {

    @Mock
    private ChairRepository chairRepository;

    @InjectMocks
    private ChairService chairService;

    private Faculty faculty;
    private Chair chair1;
    private Chair chair2;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные
        faculty = new Faculty("Факультет информатики", "ФИ");
        faculty.setId(1L);
        
        chair1 = new Chair();
        chair1.setId(1L);
        chair1.setFaculty(faculty);
        chair1.setNameChair("Кафедра программной инженерии");
        chair1.setShortNameChair("ПИ");
        
        chair2 = new Chair();
        chair2.setId(2L);
        chair2.setFaculty(faculty);
        chair2.setNameChair("Кафедра информационных систем");
        chair2.setShortNameChair("ИС");
    }

    @Test
    void testGetAllChairs() {
        // Подготовка
        List<Chair> chairs = Arrays.asList(chair1, chair2);
        when(chairRepository.findAll()).thenReturn(chairs);

        // Выполнение
        List<Chair> result = chairService.getAllChairs();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Кафедра программной инженерии", result.get(0).getNameChair());
        assertEquals("Кафедра информационных систем", result.get(1).getNameChair());
        verify(chairRepository, times(1)).findAll();
    }

    @Test
    void testGetChairById() {
        // Подготовка
        when(chairRepository.findById(1L)).thenReturn(chair1);

        // Выполнение
        Chair result = chairService.getChairById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Кафедра программной инженерии", result.getNameChair());
        assertEquals("ПИ", result.getShortNameChair());
        verify(chairRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchChairsByName() {
        // Подготовка
        String searchName = "программ";
        when(chairRepository.findByName(searchName)).thenReturn(Arrays.asList(chair1));

        // Выполнение
        List<Chair> result = chairService.searchChairsByName(searchName);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Кафедра программной инженерии", result.get(0).getNameChair());
        verify(chairRepository, times(1)).findByName(searchName);
    }

    @Test
    void testGetChairsByFaculty() {
        // Подготовка
        List<Chair> chairs = Arrays.asList(chair1, chair2);
        when(chairRepository.findByFaculty(faculty)).thenReturn(chairs);

        // Выполнение
        List<Chair> result = chairService.getChairsByFaculty(faculty);

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Кафедра программной инженерии", result.get(0).getNameChair());
        assertEquals("Кафедра информационных систем", result.get(1).getNameChair());
        verify(chairRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void testSaveChair() {
        // Выполнение
        chairService.saveChair(chair1);

        // Проверка
        verify(chairRepository, times(1)).save(chair1);
    }

    @Test
    void testDeleteChair() {
        // Выполнение
        chairService.deleteChair(1L);

        // Проверка
        verify(chairRepository, times(1)).delete(1L);
    }
} 