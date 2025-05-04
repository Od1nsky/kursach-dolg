package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.model.Discipline;
import org.kursach.kursach.repository.DisciplineRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class DisciplineService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private DisciplineRepository disciplineRepository;
    
    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }
    
    public List<Discipline> searchDisciplinesByName(String name) {
        return disciplineRepository.findByName(name);
    }
    
    public Discipline getDisciplineById(Long id) {
        return disciplineRepository.findById(id);
    }
    
    public List<Discipline> getDisciplinesByChair(Chair chair) {
        return disciplineRepository.findByChair(chair);
    }
    
    public List<Discipline> getDisciplinesByCurriculum(Curriculum curriculum) {
        return disciplineRepository.findByCurriculum(curriculum);
    }
    
    public List<Discipline> getDisciplinesByCourseAndSemester(Integer course, Integer semester) {
        return disciplineRepository.findByCourseAndSemester(course, semester);
    }
    
    public void saveDiscipline(Discipline discipline) {
        disciplineRepository.save(discipline);
    }
    
    public void deleteDiscipline(Long id) {
        disciplineRepository.delete(id);
    }
} 