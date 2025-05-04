package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.service.ChairService;
import org.kursach.kursach.service.CurriculumService;
import org.kursach.kursach.service.FacultyService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class CurriculumController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(CurriculumController.class.getName());
    
    @Inject
    private CurriculumService curriculumService;
    
    @Inject
    private FacultyService facultyService;
    
    @Inject
    private ChairService chairService;
    
    private List<Curriculum> curriculums;
    private Curriculum curriculum;
    private String searchSpeciality;
    private Integer searchCourse;
    private Long selectedFacultyId;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        curriculums = curriculumService.getAllCurriculums();
        
        // Создаем новый объект Curriculum только если текущий объект null
        if (curriculum == null) {
            curriculum = new Curriculum();
            logger.info("Создан новый объект Curriculum");
        } else {
            logger.info("Использован существующий объект Curriculum с ID: " + curriculum.getId());
        }
        
        // Пробуем загрузить из параметра
        loadCurriculum();
        
        logger.info("CurriculumController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && curriculum.getId() == null) {
                curriculum.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            logger.info("Сохранение учебного плана: " + curriculum.getNameCurriculum());
            
            // Если выбран факультет, не привязываем его напрямую, но можем использовать для других целей
            if (selectedFacultyId != null) {
                logger.info("Выбран факультет с ID: " + selectedFacultyId);
                // Можно дополнительно что-то сделать с выбранным факультетом
            }
            
            curriculumService.saveCurriculum(curriculum);
            logger.info("Учебный план сохранен успешно");
            
            // Сбрасываем editId
            editId = null;
            
            // Обновляем список учебных планов
            curriculums = curriculumService.getAllCurriculums();
            
            // Создаем новый объект для формы
            curriculum = new Curriculum();
            selectedFacultyId = null;
            
            return "curriculum?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении учебного плана: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование учебного плана с ID: " + id);
            curriculum = curriculumService.getCurriculumById(id);
            
            if (curriculum == null) {
                logger.warning("Учебный план с ID " + id + " не найден");
                return "curriculum?faces-redirect=true";
            }
            
            // Если у учебного плана есть связанная информация о факультете,
            // можно было бы установить selectedFacultyId
            // Но сейчас этой связи нет в модели Curriculum
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "curriculum-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке учебного плана для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "curriculum?faces-redirect=true";
        }
    }
    
    // Загрузка учебного плана из параметра URL
    public void loadCurriculum() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка учебного плана из параметра URL, ID: " + id);
                
                curriculum = curriculumService.getCurriculumById(id);
                if (curriculum == null) {
                    logger.warning("Учебный план с ID " + id + " не найден при загрузке из параметра");
                    curriculum = new Curriculum();
                } else {
                    logger.info("Учебный план загружен из параметра URL: " + curriculum.getNameCurriculum());
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                curriculum = new Curriculum();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление учебного плана с ID: " + id);
            curriculumService.deleteCurriculum(id);
            curriculums = curriculumService.getAllCurriculums();
            return "curriculum?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении учебного плана: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String prepareNew() {
        curriculum = new Curriculum();
        selectedFacultyId = null;
        editId = null; // Сбрасываем ID при создании нового учебного плана
        logger.info("Подготовка нового учебного плана");
        return "curriculum-edit?faces-redirect=true";
    }
    
    public void search() {
        logger.info("Поиск учебных планов по названию: " + searchTerm);
        curriculums = curriculumService.searchCurriculumsByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска учебных планов");
        searchTerm = null;
        curriculums = curriculumService.getAllCurriculums();
    }
    
    // Геттеры и сеттеры
    public List<Curriculum> getCurriculums() {
        return curriculums;
    }

    public void setCurriculums(List<Curriculum> curriculums) {
        this.curriculums = curriculums;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public String getSearchSpeciality() {
        return searchSpeciality;
    }

    public void setSearchSpeciality(String searchSpeciality) {
        this.searchSpeciality = searchSpeciality;
    }

    public Integer getSearchCourse() {
        return searchCourse;
    }

    public void setSearchCourse(Integer searchCourse) {
        this.searchCourse = searchCourse;
    }
    
    public Long getSelectedFacultyId() {
        return selectedFacultyId;
    }
    
    public void setSelectedFacultyId(Long selectedFacultyId) {
        this.selectedFacultyId = selectedFacultyId;
    }
    
    public Long getEditId() {
        return editId;
    }
    
    public void setEditId(Long editId) {
        this.editId = editId;
    }
    
    public List<Chair> getChairs() {
        return chairService.getAllChairs();
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
} 