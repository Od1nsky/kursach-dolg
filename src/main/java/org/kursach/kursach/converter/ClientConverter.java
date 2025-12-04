package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.service.ClientService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "clientConverter", managed = true)
public class ClientConverter implements Converter<Client> {
    
    private static final Logger logger = Logger.getLogger(ClientConverter.class.getName());
    
    @Inject
    private ClientService clientService;

    @Override
    public Client getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Client client = clientService.getClientById(id);
            logger.info("Converted string '" + value + "' to Client: " + (client != null ? client.getFirstName() + " " + client.getLastName() : "null"));
            return client;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Client ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Client client) {
        if (client == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            if (!(client instanceof Client)) {
                logger.warning("Ожидался объект Client, но получен: " + client.getClass().getName());
                return "";
            }
            
            if (client.getId() == null) {
                logger.warning("Client с null ID: " + client.getFirstName() + " " + client.getLastName());
                return "";
            }
            
            String result = client.getId().toString();
            logger.info("Конвертирован клиент '" + client.getFirstName() + " " + client.getLastName() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}

