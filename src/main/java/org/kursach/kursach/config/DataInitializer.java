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
                
                // Создаем 10 клиентов
                Client[] clients = new Client[10];
                clients[0] = new Client("Иван", "Иванов", "ivan.ivanov@example.com", "+79001234567", "Активен");
                clients[1] = new Client("Петр", "Петров", "petr.petrov@example.com", "+79007654321", "Активен");
                clients[2] = new Client("Мария", "Сидорова", "maria.sidorova@example.com", "+79005555555", "Неактивен");
                clients[3] = new Client("Алексей", "Смирнов", "alex.smirnov@example.com", "+79001111111", "Активен");
                clients[4] = new Client("Елена", "Козлова", "elena.kozlova@example.com", "+79002222222", "Активен");
                clients[5] = new Client("Дмитрий", "Волков", "dmitry.volkov@example.com", "+79003333333", "Активен");
                clients[6] = new Client("Анна", "Новикова", "anna.novikova@example.com", "+79004444444", "Активен");
                clients[7] = new Client("Сергей", "Морозов", "sergey.morozov@example.com", "+79005555555", "Неактивен");
                clients[8] = new Client("Ольга", "Павлова", "olga.pavlova@example.com", "+79006666666", "Активен");
                clients[9] = new Client("Николай", "Соколов", "nikolay.sokolov@example.com", "+79007777777", "Активен");
                
                em.getTransaction().begin();
                for (Client client : clients) {
                    em.persist(client);
                }
                em.getTransaction().commit();
                
                // Обновляем клиентов, чтобы получить их ID
                em.clear();
                for (int i = 0; i < clients.length; i++) {
                    clients[i] = em.find(Client.class, clients[i].getId());
                }
                
                // Создаем 10 услуг
                Service[] services = new Service[10];
                services[0] = new Service("Консультация", 1500.0);
                services[1] = new Service("Ремонт техники", 5000.0);
                services[2] = new Service("Установка ПО", 2000.0);
                services[3] = new Service("Обучение", 3000.0);
                services[4] = new Service("Техническая поддержка", 1000.0);
                services[5] = new Service("Настройка оборудования", 2500.0);
                services[6] = new Service("Диагностика", 800.0);
                services[7] = new Service("Восстановление данных", 4000.0);
                services[8] = new Service("Обслуживание серверов", 6000.0);
                services[9] = new Service("Консультация по безопасности", 3500.0);
                
                em.getTransaction().begin();
                for (Service service : services) {
                    em.persist(service);
                }
                em.getTransaction().commit();
                
                // Обновляем услуги, чтобы получить их ID
                em.clear();
                for (int i = 0; i < services.length; i++) {
                    services[i] = em.find(Service.class, services[i].getId());
                }
                for (int i = 0; i < clients.length; i++) {
                    clients[i] = em.find(Client.class, clients[i].getId());
                }
                
                // Создаем 10 покупок
                Payment[] payments = new Payment[10];
                payments[0] = new Payment(clients[0], services[0], LocalDate.now().minusDays(5), 2, 3000.0);
                payments[1] = new Payment(clients[0], services[2], LocalDate.now().minusDays(3), 1, 2000.0);
                payments[2] = new Payment(clients[1], services[1], LocalDate.now().minusDays(10), 1, 5000.0);
                payments[3] = new Payment(clients[2], services[4], LocalDate.now().minusDays(7), 3, 3000.0);
                payments[4] = new Payment(clients[3], services[0], LocalDate.now().minusDays(1), 1, 1500.0);
                payments[5] = new Payment(clients[4], services[3], LocalDate.now().minusDays(4), 2, 6000.0);
                payments[6] = new Payment(clients[5], services[5], LocalDate.now().minusDays(6), 1, 2500.0);
                payments[7] = new Payment(clients[6], services[6], LocalDate.now().minusDays(2), 2, 1600.0);
                payments[8] = new Payment(clients[7], services[7], LocalDate.now().minusDays(8), 1, 4000.0);
                payments[9] = new Payment(clients[8], services[8], LocalDate.now().minusDays(9), 1, 6000.0);
                
                em.getTransaction().begin();
                for (Payment payment : payments) {
                    em.persist(payment);
                }
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

