package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.service.ChairService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "chairConverter", managed = true)
public class ChairConverter implements Converter<Chair> {
    
    private static final Logger logger = Logger.getLogger(ChairConverter.class.getName());
    
    @Inject
    private ChairService chairService;

    @Override
    public Chair getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Chair chair = chairService.getChairById(id);
            logger.info("Converted string '" + value + "' to Chair: " + (chair != null ? chair.getNameChair() : "null"));
            return chair;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Chair ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Chair chair) {
        if (chair == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            // Защита от неправильного типа
            if (!(chair instanceof Chair)) {
                logger.warning("Ожидался объект Chair, но получен: " + chair.getClass().getName());
                return "";
            }
            
            if (chair.getId() == null) {
                logger.warning("Chair с null ID: " + chair.getNameChair());
                return "";
            }
            
            String result = chair.getId().toString();
            logger.info("Конвертирована кафедра '" + chair.getNameChair() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
} 