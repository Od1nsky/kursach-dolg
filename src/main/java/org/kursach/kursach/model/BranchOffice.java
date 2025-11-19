package org.kursach.kursach.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "branch_office")
public class BranchOffice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "manager_name", length = 120)
    private String managerName;

    @Column(name = "phone", length = 40)
    private String phone;

    @Column(name = "opened_date")
    private LocalDate openedDate;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "branchOffice")
    private List<Representative> representatives;

    public BranchOffice() {
    }

    public BranchOffice(String title, String city, Region region) {
        this.title = title;
        this.city = city;
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<Representative> getRepresentatives() {
        return representatives;
    }

    public void setRepresentatives(List<Representative> representatives) {
        this.representatives = representatives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BranchOffice that = (BranchOffice) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BranchOffice{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

