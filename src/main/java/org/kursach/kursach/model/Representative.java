package org.kursach.kursach.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "representative")
public class Representative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "position_title", length = 120)
    private String positionTitle;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "phone", length = 40)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RepresentativeStatus status = RepresentativeStatus.ACTIVE;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "specialization", length = 150)
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "branch_office_id")
    private BranchOffice branchOffice;

    @OneToMany(mappedBy = "representative")
    private List<RepresentativeReport> reports;

    public Representative() {
    }

    public Representative(String fullName, Region region, BranchOffice branchOffice) {
        this.fullName = fullName;
        this.region = region;
        this.branchOffice = branchOffice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RepresentativeStatus getStatus() {
        return status;
    }

    public void setStatus(RepresentativeStatus status) {
        this.status = status;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public BranchOffice getBranchOffice() {
        return branchOffice;
    }

    public void setBranchOffice(BranchOffice branchOffice) {
        this.branchOffice = branchOffice;
    }

    public List<RepresentativeReport> getReports() {
        return reports;
    }

    public void setReports(List<RepresentativeReport> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Representative that = (Representative) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Representative{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", status=" + status +
                '}';
    }
}

