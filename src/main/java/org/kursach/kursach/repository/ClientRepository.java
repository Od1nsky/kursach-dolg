package org.kursach.kursach.repository;

import org.kursach.kursach.model.Client;
import java.util.List;

/**
 * Интерфейс репозитория для работы с клиентами
 */
public interface ClientRepository extends Repository<Client, Long> {
    
    /**
     * Найти клиентов по имени (поиск по части имени)
     * @param name часть имени для поиска
     * @return список найденных клиентов
     */
    List<Client> findByName(String name);
}



