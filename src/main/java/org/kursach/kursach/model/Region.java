package org.kursach.kursach.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "region_code", nullable = false, unique = true, length = 20)
    private String regionCode;

    @Column(name = "curator_name", length = 120)
    private String curatorName;

    @Column(name = "contact_phone", length = 40)
    private String contactPhone;

    @Column(name = "contact_email", length = 120)
    private String contactEmail;

    @OneToMany(mappedBy = "region")
    private List<BranchOffice> branchOffices;

    @OneToMany(mappedBy = "region")
    private List<Representative> representatives;

    public Region() {
    }

    public Region(String name, String regionCode) {
        this.name = name;
        this.regionCode = regionCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCuratorName() {
        return curatorName;
    }

    public void setCuratorName(String curatorName) {
        this.curatorName = curatorName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<BranchOffice> getBranchOffices() {
        return branchOffices;
    }

    public void setBranchOffices(List<BranchOffice> branchOffices) {
        this.branchOffices = branchOffices;
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

        Region region = (Region) o;
        return id != null ? id.equals(region.id) : region.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", regionCode='" + regionCode + '\'' +
                '}';
    }
}

