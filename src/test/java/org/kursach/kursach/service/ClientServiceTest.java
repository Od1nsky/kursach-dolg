package org.kursach.kursach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.repository.ClientRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        client1 = new Client("Иван", "Иванов", "ivan@example.com", "+79001234567", "Активен");
        client1.setId(1L);
        
        client2 = new Client("Петр", "Петров", "petr@example.com", "+79007654321", "Неактивен");
        client2.setId(2L);
    }

    @Test
    void testGetAllClients() {
        // Подготовка
        List<Client> clients = Arrays.asList(client1, client2);
        when(clientRepository.findAll()).thenReturn(clients);

        // Выполнение
        List<Client> result = clientService.getAllClients();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Иван", result.get(0).getFirstName());
        assertEquals("Петр", result.get(1).getFirstName());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testSearchClientsByName() {
        // Подготовка
        String searchName = "Иван";
        when(clientRepository.findByName(searchName)).thenReturn(Arrays.asList(client1));

        // Выполнение
        List<Client> result = clientService.searchClientsByName(searchName);

        // Проверка
        assertEquals(1, result.size());
        assertEquals("Иван", result.get(0).getFirstName());
        verify(clientRepository, times(1)).findByName(searchName);
    }

    @Test
    void testGetClientById() {
        // Подготовка
        when(clientRepository.findById(1L)).thenReturn(client1);

        // Выполнение
        Client result = clientService.getClientById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Иван", result.getFirstName());
        assertEquals("Иванов", result.getLastName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveClient() {
        // Выполнение
        clientService.saveClient(client1);

        // Проверка
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void testDeleteClient() {
        // Выполнение
        clientService.deleteClient(1L);

        // Проверка
        verify(clientRepository, times(1)).delete(1L);
    }
}

