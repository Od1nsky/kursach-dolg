package org.kursach.kursach.repository;

import org.kursach.kursach.model.Curriculum;
import java.util.List;

/**
 * Интерфейс репозитория для работы с учебными планами
 */
public interface CurriculumRepository extends Repository<Curriculum, Long> {
    
    /**
     * Найти учебные планы по имени (поиск по части имени)
     * @param name часть имени для поиска
     * @return список найденных учебных планов
     */
    List<Curriculum> findByName(String name);
    
    /**
     * Найти учебные планы по курсу
     * @param course курс
     * @return список учебных планов для курса
     */
    List<Curriculum> findByCourse(Integer course);
    
    /**
     * Найти учебные планы по специальности
     * @param speciality специальность
     * @return список учебных планов для специальности
     */
    List<Curriculum> findBySpeciality(String speciality);
} 