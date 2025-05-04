package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.service.FacultyService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "facultyConverter", managed = true)
public class FacultyConverter implements Converter<Faculty> {
    
    private static final Logger logger = Logger.getLogger(FacultyConverter.class.getName());
    
    @Inject
    private FacultyService facultyService;

    @Override
    public Faculty getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Faculty faculty = facultyService.getFacultyById(id);
            logger.info("Converted string '" + value + "' to Faculty: " + (faculty != null ? faculty.getNameFaculty() : "null"));
            return faculty;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Faculty ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Faculty faculty) {
        if (faculty == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            // Защита от неправильного типа
            if (!(faculty instanceof Faculty)) {
                logger.warning("Ожидался объект Faculty, но получен: " + faculty.getClass().getName());
                return "";
            }
            
            if (faculty.getId() == null) {
                logger.warning("Faculty с null ID: " + faculty.getNameFaculty());
                return "";
            }
            
            String result = faculty.getId().toString();
            logger.info("Конвертирован факультет '" + faculty.getNameFaculty() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
} 