package org.kursach.kursach.repository;

import org.kursach.kursach.model.Faculty;
import java.util.List;

/**
 * Интерфейс репозитория для работы с факультетами
 */
public interface FacultyRepository extends Repository<Faculty, Long> {
    
    /**
     * Найти факультеты по имени (поиск по части имени)
     * @param name часть имени для поиска
     * @return список найденных факультетов
     */
    List<Faculty> findByName(String name);
} 