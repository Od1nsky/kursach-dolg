package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.service.CurriculumService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "curriculumConverter", managed = true)
public class CurriculumConverter implements Converter<Curriculum> {
    
    private static final Logger logger = Logger.getLogger(CurriculumConverter.class.getName());
    
    @Inject
    private CurriculumService curriculumService;

    @Override
    public Curriculum getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Curriculum curriculum = curriculumService.getCurriculumById(id);
            logger.info("Converted string '" + value + "' to Curriculum: " + (curriculum != null ? curriculum.getNameCurriculum() : "null"));
            return curriculum;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Curriculum ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Curriculum curriculum) {
        if (curriculum == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            // Защита от неправильного типа
            if (!(curriculum instanceof Curriculum)) {
                logger.warning("Ожидался объект Curriculum, но получен: " + curriculum.getClass().getName());
                return "";
            }
            
            if (curriculum.getId() == null) {
                logger.warning("Curriculum с null ID: " + curriculum.getNameCurriculum());
                return "";
            }
            
            String result = curriculum.getId().toString();
            logger.info("Конвертирован учебный план '" + curriculum.getNameCurriculum() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
} 