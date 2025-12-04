package org.kursach.kursach.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Service;

import java.time.LocalDate;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        try {
            // Проверяем, есть ли уже данные
            Long clientCount = em.createQuery("SELECT COUNT(c) FROM Client c", Long.class).getSingleResult();
            
            if (clientCount == 0) {
                logger.info("Инициализация тестовых данных...");
                
                // Создаем клиентов
                Client client1 = new Client("Иван", "Иванов", "ivan.ivanov@example.com", "+79001234567", "Активен");
                Client client2 = new Client("Петр", "Петров", "petr.petrov@example.com", "+79007654321", "Активен");
                Client client3 = new Client("Мария", "Сидорова", "maria.sidorova@example.com", "+79005555555", "Неактивен");
                Client client4 = new Client("Алексей", "Смирнов", "alex.smirnov@example.com", "+79001111111", "Активен");
                Client client5 = new Client("Елена", "Козлова", "elena.kozlova@example.com", "+79002222222", "Активен");
                
                em.getTransaction().begin();
                em.persist(client1);
                em.persist(client2);
                em.persist(client3);
                em.persist(client4);
                em.persist(client5);
                em.getTransaction().commit();
                
                // Обновляем клиентов, чтобы получить их ID
                em.clear();
                client1 = em.find(Client.class, client1.getId());
                client2 = em.find(Client.class, client2.getId());
                client3 = em.find(Client.class, client3.getId());
                client4 = em.find(Client.class, client4.getId());
                client5 = em.find(Client.class, client5.getId());
                
                // Создаем услуги
                Service service1 = new Service("Консультация", 1500.0);
                Service service2 = new Service("Ремонт техники", 5000.0);
                Service service3 = new Service("Установка ПО", 2000.0);
                Service service4 = new Service("Обучение", 3000.0);
                Service service5 = new Service("Техническая поддержка", 1000.0);
                
                em.getTransaction().begin();
                em.persist(service1);
                em.persist(service2);
                em.persist(service3);
                em.persist(service4);
                em.persist(service5);
                em.getTransaction().commit();
                
                // Обновляем услуги, чтобы получить их ID
                em.clear();
                service1 = em.find(Service.class, service1.getId());
                service2 = em.find(Service.class, service2.getId());
                service3 = em.find(Service.class, service3.getId());
                service4 = em.find(Service.class, service4.getId());
                service5 = em.find(Service.class, service5.getId());
                
                // Создаем покупки
                Payment payment1 = new Payment(client1, service1, LocalDate.now().minusDays(5), 2, 3000.0);
                Payment payment2 = new Payment(client1, service3, LocalDate.now().minusDays(3), 1, 2000.0);
                Payment payment3 = new Payment(client2, service2, LocalDate.now().minusDays(10), 1, 5000.0);
                Payment payment4 = new Payment(client2, service4, LocalDate.now().minusDays(2), 1, 3000.0);
                Payment payment5 = new Payment(client3, service5, LocalDate.now().minusDays(7), 3, 3000.0);
                Payment payment6 = new Payment(client4, service1, LocalDate.now().minusDays(1), 1, 1500.0);
                Payment payment7 = new Payment(client4, service2, LocalDate.now(), 1, 5000.0);
                Payment payment8 = new Payment(client5, service3, LocalDate.now().minusDays(4), 2, 4000.0);
                
                em.getTransaction().begin();
                em.persist(payment1);
                em.persist(payment2);
                em.persist(payment3);
                em.persist(payment4);
                em.persist(payment5);
                em.persist(payment6);
                em.persist(payment7);
                em.persist(payment8);
                em.getTransaction().commit();
                
                logger.info("Тестовые данные успешно инициализированы");
            } else {
                logger.info("База данных уже содержит данные, инициализация пропущена");
            }
        } catch (Exception e) {
            logger.severe("Ошибка при инициализации тестовых данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

