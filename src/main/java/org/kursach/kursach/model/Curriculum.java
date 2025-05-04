package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "curriculum")
public class Curriculum implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "academic_year")
    private String academicYear;
    
    @Column(name = "speciality")
    private String speciality;
    
    @Column(name = "qualification")
    private String qualification;
    
    @Column(name = "form_education")
    private String formEducation;
    
    @Column(name = "name_curriculum")
    private String nameCurriculum;
    
    @Column(name = "course")
    private Integer course;
    
    @OneToMany(mappedBy = "curriculum")
    private List<Discipline> disciplines;
    
    // Конструкторы
    public Curriculum() {
    }
    
    public Curriculum(String academicYear, String speciality, String qualification,
                      String formEducation, String nameCurriculum, Integer course) {
        this.academicYear = academicYear;
        this.speciality = speciality;
        this.qualification = qualification;
        this.formEducation = formEducation;
        this.nameCurriculum = nameCurriculum;
        this.course = course;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFormEducation() {
        return formEducation;
    }

    public void setFormEducation(String formEducation) {
        this.formEducation = formEducation;
    }

    public String getNameCurriculum() {
        return nameCurriculum;
    }

    public void setNameCurriculum(String nameCurriculum) {
        this.nameCurriculum = nameCurriculum;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
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
        
        Curriculum that = (Curriculum) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Curriculum{" +
                "id=" + id +
                ", academicYear='" + academicYear + '\'' +
                ", speciality='" + speciality + '\'' +
                ", qualification='" + qualification + '\'' +
                ", formEducation='" + formEducation + '\'' +
                ", nameCurriculum='" + nameCurriculum + '\'' +
                ", course=" + course +
                '}';
    }
} 