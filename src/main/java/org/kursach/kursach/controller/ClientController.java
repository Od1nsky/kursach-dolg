package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.service.ClientService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ClientController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ClientController.class.getName());
    
    @Inject
    private ClientService clientService;
    
    private List<Client> clients;
    private Client client;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        clients = clientService.getAllClients();
        
        if (client == null) {
            client = new Client();
            logger.info("Создан новый объект Client");
        } else {
            logger.info("Использован существующий объект Client с ID: " + client.getId());
        }
        
        loadClient();
        
        logger.info("ClientController инициализирован");
    }
    
    public String save() {
        try {
            if (editId != null && client.getId() == null) {
                client.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            boolean isNew = (client.getId() == null);
            logger.info((isNew ? "Создание нового" : "Обновление существующего") + " клиента: " + client.getFirstName() + " " + client.getLastName());
            
            clientService.saveClient(client);
            
            editId = null;
            clients = clientService.getAllClients();
            client = new Client();
            
            logger.info("Клиент " + (isNew ? "создан" : "обновлен") + " успешно");
            return "client?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении клиента: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование клиента с ID: " + id);
            client = clientService.getClientById(id);
            
            if (client == null) {
                logger.warning("Клиент с ID " + id + " не найден");
                return "client?faces-redirect=true";
            }
            
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            return "client-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке клиента для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "client?faces-redirect=true";
        }
    }
    
    public void loadClient() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка клиента из параметра URL, ID: " + id);
                
                client = clientService.getClientById(id);
                if (client == null) {
                    logger.warning("Клиент с ID " + id + " не найден при загрузке из параметра");
                    client = new Client();
                } else {
                    logger.info("Клиент загружен из параметра URL: " + client.getFirstName() + " " + client.getLastName());
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                client = new Client();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление клиента с ID: " + id);
            clientService.deleteClient(id);
            clients = clientService.getAllClients();
            return "client?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении клиента: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void search() {
        logger.info("Поиск клиентов по имени: " + searchTerm);
        clients = clientService.searchClientsByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска клиентов");
        searchTerm = null;
        clients = clientService.getAllClients();
    }
    
    public String prepareNew() {
        client = new Client();
        editId = null;
        logger.info("Подготовка нового клиента");
        return "client-edit?faces-redirect=true";
    }
    
    // Геттеры и сеттеры
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getEditId() {
        return editId;
    }
    
    public void setEditId(Long editId) {
        this.editId = editId;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}



