package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.model.Discipline;
import org.kursach.kursach.repository.DisciplineRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    private Chair chair;
    private Curriculum curriculum;
    private Discipline discipline1;
    private Discipline discipline2;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные
        chair = new Chair();
        chair.setId(1L);
        chair.setNameChair("Кафедра информатики");
        chair.setShortNameChair("ИНФ");

        curriculum = new Curriculum();
        curriculum.setId(1L);
        curriculum.setNameCurriculum("Программная инженерия");
        
        discipline1 = new Discipline();
        discipline1.setId(1L);
        discipline1.setNameDiscipline("Программирование");
        discipline1.setChair(chair);
        discipline1.setCurriculum(curriculum);
        discipline1.setCourse(1);
        discipline1.setSemester(1);
        discipline1.setLecture(36);
        discipline1.setLaboratory(36);
        discipline1.setPractical(0);
        discipline1.setExamen(true);
        discipline1.setSetOff(false);
        
        discipline2 = new Discipline();
        discipline2.setId(2L);
        discipline2.setNameDiscipline("Базы данных");
        discipline2.setChair(chair);
        discipline2.setCurriculum(curriculum);
        discipline2.setCourse(2);
        discipline2.setSemester(3);
        discipline2.setLecture(18);
        discipline2.setLaboratory(36);
        discipline2.setPractical(18);
        discipline2.setExamen(true);
        discipline2.setSetOff(true);
    }

    @Test
    void testGetAllDisciplines() {
        // Подготовка
        List<Discipline> disciplines = Arrays.asList(discipline1, discipline2);
        when(disciplineRepository.findAll()).thenReturn(disciplines);

        // Выполнение
        List<Discipline> result = disciplineService.getAllDisciplines();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Программирование", result.get(0).getNameDiscipline());
        assertEquals("Базы данных", result.get(1).getNameDiscipline());
        verify(disciplineRepository, times(1)).findAll();
    }

    @Test
    void testGetDisciplineById() {
        // Подготовка
        when(disciplineRepository.findById(1L)).thenReturn(discipline1);

        // Выполнение
        Discipline result = disciplineService.getDisciplineById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Программирование", result.getNameDiscipline());
        assertEquals(1, result.getCourse());
        verify(disciplineRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchDisciplinesByName() {
        // Подготовка
        String searchName = "программ";
        when(disciplineRepository.findByName(searchName)).thenReturn(Arrays.asList(discipline1));

        // Выполнение
        List<Discipline> result = disciplineService.searchDisciplinesByName(searchName);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Программирование", result.get(0).getNameDiscipline());
        verify(disciplineRepository, times(1)).findByName(searchName);
    }

    @Test
    void testGetDisciplinesByChair() {
        // Подготовка
        List<Discipline> disciplines = Arrays.asList(discipline1, discipline2);
        when(disciplineRepository.findByChair(chair)).thenReturn(disciplines);

        // Выполнение
        List<Discipline> result = disciplineService.getDisciplinesByChair(chair);

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Программирование", result.get(0).getNameDiscipline());
        verify(disciplineRepository, times(1)).findByChair(chair);
    }

    @Test
    void testGetDisciplinesByCurriculum() {
        // Подготовка
        List<Discipline> disciplines = Arrays.asList(discipline1, discipline2);
        when(disciplineRepository.findByCurriculum(curriculum)).thenReturn(disciplines);

        // Выполнение
        List<Discipline> result = disciplineService.getDisciplinesByCurriculum(curriculum);

        // Проверка
        assertEquals(2, result.size());
        verify(disciplineRepository, times(1)).findByCurriculum(curriculum);
    }

    @Test
    void testGetDisciplinesByCourseAndSemester() {
        // Подготовка
        when(disciplineRepository.findByCourseAndSemester(1, 1)).thenReturn(Arrays.asList(discipline1));

        // Выполнение
        List<Discipline> result = disciplineService.getDisciplinesByCourseAndSemester(1, 1);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Программирование", result.get(0).getNameDiscipline());
        verify(disciplineRepository, times(1)).findByCourseAndSemester(1, 1);
    }

    @Test
    void testSaveDiscipline() {
        // Выполнение
        disciplineService.saveDiscipline(discipline1);

        // Проверка
        verify(disciplineRepository, times(1)).save(discipline1);
    }

    @Test
    void testDeleteDiscipline() {
        // Выполнение
        disciplineService.deleteDiscipline(1L);

        // Проверка
        verify(disciplineRepository, times(1)).delete(1L);
    }
} 