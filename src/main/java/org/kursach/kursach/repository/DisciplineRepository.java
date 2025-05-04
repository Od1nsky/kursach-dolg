package org.kursach.kursach.repository;

import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.model.Discipline;

import java.util.List;

/**
 * Интерфейс репозитория для работы с дисциплинами
 */
public interface DisciplineRepository extends Repository<Discipline, Long> {
    
    /**
     * Найти дисциплины по имени (поиск по части имени)
     * @param name часть имени для поиска
     * @return список найденных дисциплин
     */
    List<Discipline> findByName(String name);
    
    /**
     * Найти дисциплины по кафедре
     * @param chair кафедра
     * @return список дисциплин кафедры
     */
    List<Discipline> findByChair(Chair chair);
    
    /**
     * Найти дисциплины по учебному плану
     * @param curriculum учебный план
     * @return список дисциплин учебного плана
     */
    List<Discipline> findByCurriculum(Curriculum curriculum);
    
    /**
     * Найти дисциплины по курсу и семестру
     * @param course курс
     * @param semester семестр
     * @return список дисциплин для курса и семестра
     */
    List<Discipline> findByCourseAndSemester(Integer course, Integer semester);
} 