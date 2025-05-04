package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.repository.FacultyRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class FacultyService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private FacultyRepository facultyRepository;
    
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    
    public List<Faculty> searchFacultiesByName(String name) {
        return facultyRepository.findByName(name);
    }
    
    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }
    
    public void saveFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
    }
    
    public void deleteFaculty(Long id) {
        facultyRepository.delete(id);
    }
} 