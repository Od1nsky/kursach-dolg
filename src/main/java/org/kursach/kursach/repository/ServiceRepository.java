package org.kursach.kursach.repository;

import org.kursach.kursach.model.Service;
import java.util.List;

/**
 * Интерфейс репозитория для работы с услугами
 */
public interface ServiceRepository extends Repository<Service, Long> {
    
    /**
     * Найти услуги по имени (поиск по части имени)
     * @param name часть имени для поиска
     * @return список найденных услуг
     */
    List<Service> findByName(String name);
}



