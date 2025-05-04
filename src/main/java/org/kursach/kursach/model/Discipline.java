package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "discipline")
public class Discipline implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_chair")
    private Chair chair;
    
    @ManyToOne
    @JoinColumn(name = "id_curriculum")
    private Curriculum curriculum;
    
    @Column(name = "name_discipline")
    private String nameDiscipline;
    
    private Integer course;
    
    private Integer semester;
    
    private Integer lecture;
    
    private Integer laboratory;
    
    private Integer practical;
    
    private Boolean examen;
    
    @Column(name = "set_off")
    private Boolean setOff;
    
    // Конструкторы
    public Discipline() {
    }
    
    public Discipline(Chair chair, Curriculum curriculum, String nameDiscipline, 
                     Integer course, Integer semester, Integer lecture, 
                     Integer laboratory, Integer practical, Boolean examen, Boolean setOff) {
        this.chair = chair;
        this.curriculum = curriculum;
        this.nameDiscipline = nameDiscipline;
        this.course = course;
        this.semester = semester;
        this.lecture = lecture;
        this.laboratory = laboratory;
        this.practical = practical;
        this.examen = examen;
        this.setOff = setOff;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chair getChair() {
        return chair;
    }

    public void setChair(Chair chair) {
        this.chair = chair;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public String getNameDiscipline() {
        return nameDiscipline;
    }

    public void setNameDiscipline(String nameDiscipline) {
        this.nameDiscipline = nameDiscipline;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getLecture() {
        return lecture;
    }

    public void setLecture(Integer lecture) {
        this.lecture = lecture;
    }

    public Integer getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Integer laboratory) {
        this.laboratory = laboratory;
    }

    public Integer getPractical() {
        return practical;
    }

    public void setPractical(Integer practical) {
        this.practical = practical;
    }

    public Boolean getExamen() {
        return examen;
    }

    public void setExamen(Boolean examen) {
        this.examen = examen;
    }

    public Boolean getSetOff() {
        return setOff;
    }

    public void setSetOff(Boolean setOff) {
        this.setOff = setOff;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Discipline that = (Discipline) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", chair=" + (chair != null ? chair.getShortNameChair() : "null") +
                ", curriculum=" + (curriculum != null ? curriculum.getNameCurriculum() : "null") +
                ", nameDiscipline='" + nameDiscipline + '\'' +
                ", course=" + course +
                ", semester=" + semester +
                ", lecture=" + lecture +
                ", laboratory=" + laboratory +
                ", practical=" + practical +
                ", examen=" + examen +
                ", setOff=" + setOff +
                '}';
    }
} 