package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Service;
import org.kursach.kursach.repository.ServiceRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    private Service service1;
    private Service service2;

    @BeforeEach
    void setUp() {
        service1 = new Service("Консультация", 1000.0);
        service1.setId(1L);
        
        service2 = new Service("Ремонт", 5000.0);
        service2.setId(2L);
    }

    @Test
    void testGetAllServices() {
        // Подготовка
        List<Service> services = Arrays.asList(service1, service2);
        when(serviceRepository.findAll()).thenReturn(services);

        // Выполнение
        List<Service> result = serviceService.getAllServices();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Консультация", result.get(0).getName());
        assertEquals("Ремонт", result.get(1).getName());
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void testSearchServicesByName() {
        // Подготовка
        String searchName = "Консультация";
        when(serviceRepository.findByName(searchName)).thenReturn(Arrays.asList(service1));

        // Выполнение
        List<Service> result = serviceService.searchServicesByName(searchName);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Консультация", result.get(0).getName());
        verify(serviceRepository, times(1)).findByName(searchName);
    }

    @Test
    void testGetServiceById() {
        // Подготовка
        when(serviceRepository.findById(1L)).thenReturn(service1);

        // Выполнение
        Service result = serviceService.getServiceById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Консультация", result.getName());
        assertEquals(1000.0, result.getPrice());
        verify(serviceRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveService() {
        // Выполнение
        serviceService.saveService(service1);

        // Проверка
        verify(serviceRepository, times(1)).save(service1);
    }

    @Test
    void testDeleteService() {
        // Выполнение
        serviceService.deleteService(1L);

        // Проверка
        verify(serviceRepository, times(1)).delete(1L);
    }
}


