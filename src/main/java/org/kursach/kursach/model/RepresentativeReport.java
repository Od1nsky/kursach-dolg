package org.kursach.kursach.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "representative_report")
public class RepresentativeReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "representative_id")
    private Representative representative;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "new_clients")
    private Integer newClients;

    @Column(name = "sales_volume")
    private BigDecimal salesVolume;

    @Column(name = "meetings_held")
    private Integer meetingsHeld;

    @Column(name = "focus_products", length = 200)
    private String focusProducts;

    @Column(name = "key_issues", length = 400)
    private String keyIssues;

    @Column(name = "summary", length = 400)
    private String summary;

    public RepresentativeReport() {
    }

    public RepresentativeReport(Representative representative, LocalDate periodStart, LocalDate periodEnd) {
        this.representative = representative;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Integer getNewClients() {
        return newClients;
    }

    public void setNewClients(Integer newClients) {
        this.newClients = newClients;
    }

    public BigDecimal getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(BigDecimal salesVolume) {
        this.salesVolume = salesVolume;
    }

    public Integer getMeetingsHeld() {
        return meetingsHeld;
    }

    public void setMeetingsHeld(Integer meetingsHeld) {
        this.meetingsHeld = meetingsHeld;
    }

    public String getFocusProducts() {
        return focusProducts;
    }

    public void setFocusProducts(String focusProducts) {
        this.focusProducts = focusProducts;
    }

    public String getKeyIssues() {
        return keyIssues;
    }

    public void setKeyIssues(String keyIssues) {
        this.keyIssues = keyIssues;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RepresentativeReport that = (RepresentativeReport) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RepresentativeReport{" +
                "id=" + id +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                '}';
    }
}

