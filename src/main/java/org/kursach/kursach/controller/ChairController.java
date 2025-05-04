package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.service.ChairService;
import org.kursach.kursach.service.FacultyService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ChairController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ChairController.class.getName());
    
    @Inject
    private ChairService chairService;
    
    @Inject
    private FacultyService facultyService;
    
    private List<Chair> chairs;
    private Chair chair;
    private Long selectedFacultyId;
    private List<Faculty> faculties;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        chairs = chairService.getAllChairs();
        
        // Создаем новый объект Chair только если текущий объект null
        if (chair == null) {
            chair = new Chair();
            logger.info("Создан новый объект Chair");
        } else {
            logger.info("Использован существующий объект Chair с ID: " + chair.getId());
        }
        
        // Загружаем список факультетов
        faculties = facultyService.getAllFaculties();
        
        // Пробуем загрузить из параметра
        loadChair();
        
        logger.info("ChairController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && chair.getId() == null) {
                chair.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            logger.info("Сохранение кафедры: " + chair.getNameChair());
            
            // Устанавливаем факультет по ID, даже если он уже был установлен
            // Это нужно для случаев, когда пользователь изменил выбор факультета
            if (selectedFacultyId != null) {
                Faculty faculty = facultyService.getFacultyById(selectedFacultyId);
                if (faculty != null) {
                    chair.setFaculty(faculty);
                    logger.info("Установлен факультет: " + faculty.getNameFaculty() + " (ID: " + selectedFacultyId + ")");
                } else {
                    logger.warning("Не удалось найти факультет с ID: " + selectedFacultyId);
                }
            } else {
                logger.warning("ID факультета не выбран");
                return null; // Предотвращаем сохранение без выбранного факультета
            }
            
            chairService.saveChair(chair);
            logger.info("Кафедра сохранена успешно");
            
            // Сбрасываем editId
            editId = null;
            
            // Обновляем список кафедр
            chairs = chairService.getAllChairs();
            // Создаем новый объект для формы
            chair = new Chair();
            selectedFacultyId = null;
            
            return "chair?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении кафедры: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование кафедры с ID: " + id);
            chair = chairService.getChairById(id);
            
            if (chair == null) {
                logger.warning("Кафедра с ID " + id + " не найдена");
                return "chair?faces-redirect=true";
            }
            
            if (chair.getFaculty() != null) {
                selectedFacultyId = chair.getFaculty().getId();
                logger.info("Установлен ID факультета: " + selectedFacultyId);
            }
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "chair-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке кафедры для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "chair?faces-redirect=true";
        }
    }
    
    // Загрузка кафедры из параметра URL
    public void loadChair() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка кафедры из параметра URL, ID: " + id);
                
                chair = chairService.getChairById(id);
                if (chair == null) {
                    logger.warning("Кафедра с ID " + id + " не найдена при загрузке из параметра");
                    chair = new Chair();
                } else {
                    logger.info("Кафедра загружена из параметра URL: " + chair.getNameChair());
                    if (chair.getFaculty() != null) {
                        selectedFacultyId = chair.getFaculty().getId();
                        logger.info("Установлен ID факультета: " + selectedFacultyId);
                    }
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                chair = new Chair();
            }
        }
    }
    
    public String delete(Long id) {
        logger.info("Удаление кафедры с ID: " + id);
        chairService.deleteChair(id);
        chairs = chairService.getAllChairs();
        return "chair?faces-redirect=true";
    }
    
    public String prepareNew() {
        chair = new Chair();
        selectedFacultyId = null;
        editId = null; // Сбрасываем ID при создании новой кафедры
        logger.info("Подготовка новой кафедры");
        return "chair-edit?faces-redirect=true";
    }
    
    public void filterByFaculty() {
        if (selectedFacultyId != null) {
            Faculty faculty = facultyService.getFacultyById(selectedFacultyId);
            chairs = chairService.getChairsByFaculty(faculty);
        } else {
            chairs = chairService.getAllChairs();
        }
    }
    
    public void search() {
        logger.info("Поиск кафедр по названию: " + searchTerm);
        chairs = chairService.searchChairsByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска кафедр");
        searchTerm = null;
        chairs = chairService.getAllChairs();
    }
    
    // Геттеры и сеттеры
    public List<Chair> getChairs() {
        return chairs;
    }

    public void setChairs(List<Chair> chairs) {
        this.chairs = chairs;
    }

    public Chair getChair() {
        return chair;
    }

    public void setChair(Chair chair) {
        this.chair = chair;
    }

    public Long getSelectedFacultyId() {
        return selectedFacultyId;
    }

    public void setSelectedFacultyId(Long selectedFacultyId) {
        this.selectedFacultyId = selectedFacultyId;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
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