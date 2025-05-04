package org.kursach.kursach.repository;

import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Faculty;

import java.util.List;

/**
 * Интерфейс репозитория для работы с кафедрами
 */
public interface ChairRepository extends Repository<Chair, Long> {
    
    /**
     * Найти кафедры по имени (поиск по части имени)
     * @param name часть имени для поиска
     * @return список найденных кафедр
     */
    List<Chair> findByName(String name);
    
    /**
     * Найти кафедры по факультету
     * @param faculty факультет
     * @return список кафедр факультета
     */
    List<Chair> findByFaculty(Faculty faculty);
} 