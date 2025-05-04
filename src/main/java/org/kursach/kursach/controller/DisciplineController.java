package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.model.Discipline;
import org.kursach.kursach.service.ChairService;
import org.kursach.kursach.service.CurriculumService;
import org.kursach.kursach.service.DisciplineService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class DisciplineController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(DisciplineController.class.getName());
    
    @Inject
    private DisciplineService disciplineService;
    
    @Inject
    private ChairService chairService;
    
    @Inject
    private CurriculumService curriculumService;
    
    private List<Discipline> disciplines;
    private Discipline discipline;
    private Long selectedChairId;
    private Long selectedCurriculumId;
    private List<Chair> chairs;
    private List<Curriculum> curriculums;
    private Integer searchCourse;
    private Integer searchSemester;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        disciplines = disciplineService.getAllDisciplines();
        
        // Создаем новый объект Discipline только если текущий объект null
        if (discipline == null) {
            discipline = new Discipline();
            logger.info("Создан новый объект Discipline");
        } else {
            logger.info("Использован существующий объект Discipline с ID: " + discipline.getId());
        }
        
        chairs = chairService.getAllChairs();
        curriculums = curriculumService.getAllCurriculums();
        
        // Пробуем загрузить из параметра
        loadDiscipline();
        
        logger.info("DisciplineController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && discipline.getId() == null) {
                discipline.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            logger.info("Сохранение дисциплины: " + discipline.getNameDiscipline());
            
            // Устанавливаем кафедру по ID, если выбрана
            if (selectedChairId != null) {
                Chair chair = chairService.getChairById(selectedChairId);
                if (chair != null) {
                    discipline.setChair(chair);
                    logger.info("Установлена кафедра: " + chair.getNameChair() + " (ID: " + selectedChairId + ")");
                } else {
                    logger.warning("Не удалось найти кафедру с ID: " + selectedChairId);
                }
            }
            
            // Устанавливаем учебный план по ID, если выбран
            if (selectedCurriculumId != null) {
                Curriculum curriculum = curriculumService.getCurriculumById(selectedCurriculumId);
                if (curriculum != null) {
                    discipline.setCurriculum(curriculum);
                    logger.info("Установлен учебный план: " + curriculum.getNameCurriculum() + " (ID: " + selectedCurriculumId + ")");
                } else {
                    logger.warning("Не удалось найти учебный план с ID: " + selectedCurriculumId);
                }
            }
            
            disciplineService.saveDiscipline(discipline);
            logger.info("Дисциплина сохранена успешно");
            
            // Сбрасываем editId
            editId = null;
            
            // Обновляем список дисциплин
            disciplines = disciplineService.getAllDisciplines();
            
            // Создаем новый объект для формы
            discipline = new Discipline();
            selectedChairId = null;
            selectedCurriculumId = null;
            
            return "discipline?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении дисциплины: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование дисциплины с ID: " + id);
            discipline = disciplineService.getDisciplineById(id);
            
            if (discipline == null) {
                logger.warning("Дисциплина с ID " + id + " не найдена");
                return "discipline?faces-redirect=true";
            }
            
            if (discipline.getChair() != null) {
                selectedChairId = discipline.getChair().getId();
                logger.info("Установлен ID кафедры: " + selectedChairId);
            }
            
            if (discipline.getCurriculum() != null) {
                selectedCurriculumId = discipline.getCurriculum().getId();
                logger.info("Установлен ID учебного плана: " + selectedCurriculumId);
            }
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "discipline-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке дисциплины для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "discipline?faces-redirect=true";
        }
    }
    
    // Загрузка дисциплины из параметра URL
    public void loadDiscipline() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка дисциплины из параметра URL, ID: " + id);
                
                discipline = disciplineService.getDisciplineById(id);
                if (discipline == null) {
                    logger.warning("Дисциплина с ID " + id + " не найдена при загрузке из параметра");
                    discipline = new Discipline();
                } else {
                    logger.info("Дисциплина загружена из параметра URL: " + discipline.getNameDiscipline());
                    
                    if (discipline.getChair() != null) {
                        selectedChairId = discipline.getChair().getId();
                        logger.info("Установлен ID кафедры: " + selectedChairId);
                    }
                    
                    if (discipline.getCurriculum() != null) {
                        selectedCurriculumId = discipline.getCurriculum().getId();
                        logger.info("Установлен ID учебного плана: " + selectedCurriculumId);
                    }
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                discipline = new Discipline();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление дисциплины с ID: " + id);
            disciplineService.deleteDiscipline(id);
            disciplines = disciplineService.getAllDisciplines();
            return "discipline?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении дисциплины: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String prepareNew() {
        discipline = new Discipline();
        selectedChairId = null;
        selectedCurriculumId = null;
        editId = null; // Сбрасываем ID при создании новой дисциплины
        logger.info("Подготовка новой дисциплины");
        return "discipline-edit?faces-redirect=true";
    }
    
    public void filterByChair() {
        if (selectedChairId != null) {
            Chair chair = chairService.getChairById(selectedChairId);
            disciplines = disciplineService.getDisciplinesByChair(chair);
        } else {
            disciplines = disciplineService.getAllDisciplines();
        }
    }
    
    public void filterByCurriculum() {
        if (selectedCurriculumId != null) {
            Curriculum curriculum = curriculumService.getCurriculumById(selectedCurriculumId);
            disciplines = disciplineService.getDisciplinesByCurriculum(curriculum);
        } else {
            disciplines = disciplineService.getAllDisciplines();
        }
    }
    
    public void filterByCourseAndSemester() {
        if (searchCourse != null && searchSemester != null) {
            disciplines = disciplineService.getDisciplinesByCourseAndSemester(searchCourse, searchSemester);
        } else {
            disciplines = disciplineService.getAllDisciplines();
        }
    }
    
    public void clearFilters() {
        selectedChairId = null;
        selectedCurriculumId = null;
        searchCourse = null;
        searchSemester = null;
        disciplines = disciplineService.getAllDisciplines();
    }
    
    public void search() {
        logger.info("Поиск дисциплин по названию: " + searchTerm);
        disciplines = disciplineService.searchDisciplinesByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска дисциплин");
        searchTerm = null;
        disciplines = disciplineService.getAllDisciplines();
    }
    
    // Геттеры и сеттеры
    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Long getSelectedChairId() {
        return selectedChairId;
    }

    public void setSelectedChairId(Long selectedChairId) {
        this.selectedChairId = selectedChairId;
    }

    public Long getSelectedCurriculumId() {
        return selectedCurriculumId;
    }

    public void setSelectedCurriculumId(Long selectedCurriculumId) {
        this.selectedCurriculumId = selectedCurriculumId;
    }

    public List<Chair> getChairs() {
        return chairs;
    }

    public void setChairs(List<Chair> chairs) {
        this.chairs = chairs;
    }

    public List<Curriculum> getCurriculums() {
        return curriculums;
    }

    public void setCurriculums(List<Curriculum> curriculums) {
        this.curriculums = curriculums;
    }

    public Integer getSearchCourse() {
        return searchCourse;
    }

    public void setSearchCourse(Integer searchCourse) {
        this.searchCourse = searchCourse;
    }

    public Integer getSearchSemester() {
        return searchSemester;
    }

    public void setSearchSemester(Integer searchSemester) {
        this.searchSemester = searchSemester;
    }
    
    public Long getEditId() {
        return editId;
    }
    
    public void setEditId(Long editId) {
        this.editId = editId;
    }
    
    // Метод для получения дисциплин по учебному плану
    public List<Discipline> getDisciplinesByCurriculum(Curriculum curriculum) {
        if (curriculum == null || curriculum.getId() == null) {
            return List.of(); // Возвращаем пустой список, если учебный план не задан
        }
        return disciplineService.getDisciplinesByCurriculum(curriculum);
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
} 