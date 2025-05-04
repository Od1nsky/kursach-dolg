package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Chair;
import org.kursach.kursach.model.Faculty;
import org.kursach.kursach.repository.ChairRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ChairService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private ChairRepository chairRepository;
    
    public List<Chair> getAllChairs() {
        return chairRepository.findAll();
    }
    
    public List<Chair> searchChairsByName(String name) {
        return chairRepository.findByName(name);
    }
    
    public Chair getChairById(Long id) {
        return chairRepository.findById(id);
    }
    
    public List<Chair> getChairsByFaculty(Faculty faculty) {
        return chairRepository.findByFaculty(faculty);
    }
    
    public void saveChair(Chair chair) {
        chairRepository.save(chair);
    }
    
    public void deleteChair(Long id) {
        chairRepository.delete(id);
    }
} 