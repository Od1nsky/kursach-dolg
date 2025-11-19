package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Region;
import org.kursach.kursach.service.RegionService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "regionConverter", managed = true)
public class RegionConverter implements Converter<Region> {

    private static final Logger logger = Logger.getLogger(RegionConverter.class.getName());

    @Inject
    private RegionService regionService;

    @Override
    public Region getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return regionService.getRegion(id);
        } catch (NumberFormatException e) {
            logger.warning("Не удалось преобразовать значение в Region: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Region region) {
        if (region == null || region.getId() == null) {
            return "";
        }
        return region.getId().toString();
    }
}

