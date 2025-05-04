package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "faculty")
public class Faculty implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name_faculty")
    private String nameFaculty;
    
    @Column(name = "short_name_faculty")
    private String shortNameFaculty;
    
    @OneToMany(mappedBy = "faculty")
    private List<Chair> chairs;
    
    // Конструкторы
    public Faculty() {
    }
    
    public Faculty(String nameFaculty, String shortNameFaculty) {
        this.nameFaculty = nameFaculty;
        this.shortNameFaculty = shortNameFaculty;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameFaculty() {
        return nameFaculty;
    }

    public void setNameFaculty(String nameFaculty) {
        this.nameFaculty = nameFaculty;
    }

    public String getShortNameFaculty() {
        return shortNameFaculty;
    }

    public void setShortNameFaculty(String shortNameFaculty) {
        this.shortNameFaculty = shortNameFaculty;
    }

    public List<Chair> getChairs() {
        return chairs;
    }

    public void setChairs(List<Chair> chairs) {
        this.chairs = chairs;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Faculty faculty = (Faculty) o;
        return id != null ? id.equals(faculty.id) : faculty.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", nameFaculty='" + nameFaculty + '\'' +
                ", shortNameFaculty='" + shortNameFaculty + '\'' +
                '}';
    }
} 