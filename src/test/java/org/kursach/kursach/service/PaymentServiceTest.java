package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Service;
import org.kursach.kursach.repository.PaymentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Client client;
    private Service service;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        client = new Client("Иван", "Иванов", "ivan@example.com", "+79001234567", "Активен");
        client.setId(1L);
        
        service = new Service("Консультация", 1000.0);
        service.setId(1L);
        
        payment1 = new Payment(client, service, LocalDate.now(), 2, 2000.0);
        payment1.setId(1L);
        
        payment2 = new Payment(client, service, LocalDate.now().minusDays(1), 1, 1000.0);
        payment2.setId(2L);
    }

    @Test
    void testGetAllPayments() {
        // Подготовка
        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        // Выполнение
        List<Payment> result = paymentService.getAllPayments();

        // Проверка
        assertEquals(2, result.size());
        assertEquals(2000.0, result.get(0).getAmount());
        assertEquals(1000.0, result.get(1).getAmount());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testGetPaymentsByClientId() {
        // Подготовка
        when(paymentRepository.findByClientId(1L)).thenReturn(Arrays.asList(payment1, payment2));

        // Выполнение
        List<Payment> result = paymentService.getPaymentsByClientId(1L);

        // Проверка
        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findByClientId(1L);
    }

    @Test
    void testGetPaymentsByServiceId() {
        // Подготовка
        when(paymentRepository.findByServiceId(1L)).thenReturn(Arrays.asList(payment1, payment2));

        // Выполнение
        List<Payment> result = paymentService.getPaymentsByServiceId(1L);

        // Проверка
        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findByServiceId(1L);
    }

    @Test
    void testGetPaymentById() {
        // Подготовка
        when(paymentRepository.findById(1L)).thenReturn(payment1);

        // Выполнение
        Payment result = paymentService.getPaymentById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals(2000.0, result.getAmount());
        assertEquals(2, result.getQuantity());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void testSavePayment() {
        // Выполнение
        paymentService.savePayment(payment1);

        // Проверка
        verify(paymentRepository, times(1)).save(payment1);
    }

    @Test
    void testDeletePayment() {
        // Выполнение
        paymentService.deletePayment(1L);

        // Проверка
        verify(paymentRepository, times(1)).delete(1L);
    }
}



