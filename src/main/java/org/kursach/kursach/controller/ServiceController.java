package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Service;
import org.kursach.kursach.service.ServiceService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ServiceController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ServiceController.class.getName());
    
    @Inject
    private ServiceService serviceService;
    
    private List<Service> services;
    private Service service;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        services = serviceService.getAllServices();
        
        if (service == null) {
            service = new Service();
            logger.info("Создан новый объект Service");
        } else {
            logger.info("Использован существующий объект Service с ID: " + service.getId());
        }
        
        loadService();
        
        logger.info("ServiceController инициализирован");
    }
    
    public String save() {
        try {
            if (editId != null && service.getId() == null) {
                service.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            boolean isNew = (service.getId() == null);
            logger.info((isNew ? "Создание новой" : "Обновление существующей") + " услуги: " + service.getName());
            
            serviceService.saveService(service);
            
            editId = null;
            services = serviceService.getAllServices();
            service = new Service();
            
            logger.info("Услуга " + (isNew ? "создана" : "обновлена") + " успешно");
            return "service?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении услуги: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование услуги с ID: " + id);
            service = serviceService.getServiceById(id);
            
            if (service == null) {
                logger.warning("Услуга с ID " + id + " не найдена");
                return "service?faces-redirect=true";
            }
            
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            return "service-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке услуги для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "service?faces-redirect=true";
        }
    }
    
    public void loadService() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка услуги из параметра URL, ID: " + id);
                
                service = serviceService.getServiceById(id);
                if (service == null) {
                    logger.warning("Услуга с ID " + id + " не найдена при загрузке из параметра");
                    service = new Service();
                } else {
                    logger.info("Услуга загружена из параметра URL: " + service.getName());
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                service = new Service();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление услуги с ID: " + id);
            serviceService.deleteService(id);
            services = serviceService.getAllServices();
            return "service?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении услуги: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void search() {
        logger.info("Поиск услуг по названию: " + searchTerm);
        services = serviceService.searchServicesByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска услуг");
        searchTerm = null;
        services = serviceService.getAllServices();
    }
    
    public String prepareNew() {
        service = new Service();
        editId = null;
        logger.info("Подготовка новой услуги");
        return "service-edit?faces-redirect=true";
    }
    
    // Геттеры и сеттеры
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

