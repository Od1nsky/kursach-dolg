package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Representative;
import org.kursach.kursach.service.RepresentativeService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "representativeConverter", managed = true)
public class RepresentativeConverter implements Converter<Representative> {

    private static final Logger logger = Logger.getLogger(RepresentativeConverter.class.getName());

    @Inject
    private RepresentativeService representativeService;

    @Override
    public Representative getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return representativeService.getRepresentative(id);
        } catch (NumberFormatException e) {
            logger.warning("Не удалось преобразовать значение в Representative: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Representative representative) {
        if (representative == null || representative.getId() == null) {
            return "";
        }
        return representative.getId().toString();
    }
}

