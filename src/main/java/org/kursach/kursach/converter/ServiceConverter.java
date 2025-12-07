package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Service;
import org.kursach.kursach.service.ServiceService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "serviceConverter", managed = true)
public class ServiceConverter implements Converter<Service> {
    
    private static final Logger logger = Logger.getLogger(ServiceConverter.class.getName());
    
    @Inject
    private ServiceService serviceService;

    @Override
    public Service getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Service service = serviceService.getServiceById(id);
            logger.info("Converted string '" + value + "' to Service: " + (service != null ? service.getName() : "null"));
            return service;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Service ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Service service) {
        if (service == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            if (!(service instanceof Service)) {
                logger.warning("Ожидался объект Service, но получен: " + service.getClass().getName());
                return "";
            }
            
            if (service.getId() == null) {
                logger.warning("Service с null ID: " + service.getName());
                return "";
            }
            
            String result = service.getId().toString();
            logger.info("Конвертирована услуга '" + service.getName() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}


