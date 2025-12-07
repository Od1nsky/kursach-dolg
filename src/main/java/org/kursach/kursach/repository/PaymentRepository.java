package org.kursach.kursach.repository;

import org.kursach.kursach.model.Payment;
import java.util.List;

/**
 * Интерфейс репозитория для работы с покупками
 */
public interface PaymentRepository extends Repository<Payment, Long> {
    
    /**
     * Найти покупки по клиенту
     * @param clientId идентификатор клиента
     * @return список покупок клиента
     */
    List<Payment> findByClientId(Long clientId);
    
    /**
     * Найти покупки по услуге
     * @param serviceId идентификатор услуги
     * @return список покупок услуги
     */
    List<Payment> findByServiceId(Long serviceId);
}


