package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.service.FacultyService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class FacultyController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FacultyController.class.getName());
    
    @Inject
    private FacultyService facultyService;
    
    private List<Faculty> faculties;
    private Faculty faculty;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        faculties = facultyService.getAllFaculties();
        
        // Создаем новый Faculty только если текущий объект null
        if (faculty == null) {
            faculty = new Faculty();
            logger.info("Создан новый объект Faculty");
        } else {
            logger.info("Использован существующий объект Faculty с ID: " + faculty.getId());
        }
        
        // Пробуем загрузить из параметра
        loadFaculty();
        
        logger.info("FacultyController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && faculty.getId() == null) {
                faculty.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            boolean isNew = (faculty.getId() == null);
            logger.info((isNew ? "Создание нового" : "Обновление существующего") + " факультета: " + faculty.getNameFaculty());
            
            if (!isNew) {
                logger.info("ID редактируемого факультета: " + faculty.getId());
            }
            
            // Добавляем логирование ID перед сохранением
            logger.info("Перед сохранением факультета - ID: " + faculty.getId() + 
                         ", Название: " + faculty.getNameFaculty() + 
                         ", Короткое название: " + faculty.getShortNameFaculty());
            
            facultyService.saveFaculty(faculty);
            
            // Логируем ID после сохранения
            logger.info("После сохранения - статус операции: успешно");
            
            // Сбрасываем editId
            editId = null;
            
            // Обновляем список факультетов
            faculties = facultyService.getAllFaculties();
            // Создаем новый объект для формы
            faculty = new Faculty();
            
            logger.info("Факультет " + (isNew ? "создан" : "обновлен") + " успешно");
            return "faculty?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении факультета: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование факультета с ID: " + id);
            faculty = facultyService.getFacultyById(id);
            
            if (faculty == null) {
                logger.warning("Факультет с ID " + id + " не найден");
                return "faculty?faces-redirect=true";
            }
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "faculty-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке факультета для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "faculty?faces-redirect=true";
        }
    }
    
    // Загрузка факультета из параметра URL
    public void loadFaculty() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка факультета из параметра URL, ID: " + id);
                
                faculty = facultyService.getFacultyById(id);
                if (faculty == null) {
                    logger.warning("Факультет с ID " + id + " не найден при загрузке из параметра");
                    faculty = new Faculty();
                } else {
                    logger.info("Факультет загружен из параметра URL: " + faculty.getNameFaculty());
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                faculty = new Faculty();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление факультета с ID: " + id);
            facultyService.deleteFaculty(id);
            faculties = facultyService.getAllFaculties();
            return "faculty?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении факультета: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void search() {
        logger.info("Поиск факультетов по названию: " + searchTerm);
        faculties = facultyService.searchFacultiesByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска факультетов");
        searchTerm = null;
        faculties = facultyService.getAllFaculties();
    }
    
    public String prepareNew() {
        faculty = new Faculty();
        editId = null; // Сбрасываем ID при создании нового факультета
        logger.info("Подготовка новой формы факультета");
        // Перенаправляем без параметра id
        return "faculty-edit?faces-redirect=true";
    }
    
    // Геттеры и сеттеры
    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
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