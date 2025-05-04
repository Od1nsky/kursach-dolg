package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Curriculum;
import org.kursach.kursach.repository.CurriculumRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class CurriculumService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private CurriculumRepository curriculumRepository;
    
    public List<Curriculum> getAllCurriculums() {
        return curriculumRepository.findAll();
    }
    
    public List<Curriculum> searchCurriculumsByName(String name) {
        return curriculumRepository.findByName(name);
    }
    
    public Curriculum getCurriculumById(Long id) {
        return curriculumRepository.findById(id);
    }
    
    public List<Curriculum> getCurriculumsByCourse(Integer course) {
        return curriculumRepository.findByCourse(course);
    }
    
    public List<Curriculum> getCurriculumsBySpeciality(String speciality) {
        return curriculumRepository.findBySpeciality(speciality);
    }
    
    public void saveCurriculum(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }
    
    public void deleteCurriculum(Long id) {
        curriculumRepository.delete(id);
    }
} 