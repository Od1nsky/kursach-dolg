package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "chair")
public class Chair implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_faculty")
    private Faculty faculty;
    
    @Column(name = "name_chair")
    private String nameChair;
    
    @Column(name = "short_name_chair")
    private String shortNameChair;
    
    @OneToMany(mappedBy = "chair")
    private List<Discipline> disciplines;
    
    // Конструкторы
    public Chair() {
    }
    
    public Chair(Faculty faculty, String nameChair, String shortNameChair) {
        this.faculty = faculty;
        this.nameChair = nameChair;
        this.shortNameChair = shortNameChair;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getNameChair() {
        return nameChair;
    }

    public void setNameChair(String nameChair) {
        this.nameChair = nameChair;
    }

    public String getShortNameChair() {
        return shortNameChair;
    }

    public void setShortNameChair(String shortNameChair) {
        this.shortNameChair = shortNameChair;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Chair chair = (Chair) o;
        return id != null ? id.equals(chair.id) : chair.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Chair{" +
                "id=" + id +
                ", faculty=" + (faculty != null ? faculty.getShortNameFaculty() : "null") +
                ", nameChair='" + nameChair + '\'' +
                ", shortNameChair='" + shortNameChair + '\'' +
                '}';
    }
} 