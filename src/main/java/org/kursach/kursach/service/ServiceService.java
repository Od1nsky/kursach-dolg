package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Service;
import org.kursach.kursach.repository.ServiceRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ServiceService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private ServiceRepository serviceRepository;
    
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
    
    public List<Service> searchServicesByName(String name) {
        return serviceRepository.findByName(name);
    }
    
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id);
    }
    
    public void saveService(Service service) {
        serviceRepository.save(service);
    }
    
    public void deleteService(Long id) {
        serviceRepository.delete(id);
    }
}



