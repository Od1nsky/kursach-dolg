package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.BranchOffice;
import org.kursach.kursach.service.BranchOfficeService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "branchOfficeConverter", managed = true)
public class BranchOfficeConverter implements Converter<BranchOffice> {

    private static final Logger logger = Logger.getLogger(BranchOfficeConverter.class.getName());

    @Inject
    private BranchOfficeService branchOfficeService;

    @Override
    public BranchOffice getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return branchOfficeService.getOffice(id);
        } catch (NumberFormatException e) {
            logger.warning("Не удалось преобразовать значение в BranchOffice: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BranchOffice office) {
        if (office == null || office.getId() == null) {
            return "";
        }
        return office.getId().toString();
    }
}

