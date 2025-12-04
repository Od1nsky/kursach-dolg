package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "amount")
    private Double amount;
    
    // Конструкторы
    public Payment() {
    }
    
    public Payment(Client client, Service service, LocalDate date, Integer quantity, Double amount) {
        this.client = client;
        this.service = service;
        this.date = date;
        this.quantity = quantity;
        this.amount = amount;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Payment payment = (Payment) o;
        return id != null ? id.equals(payment.id) : payment.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", client=" + (client != null ? client.getId() : null) +
                ", service=" + (service != null ? service.getId() : null) +
                ", date=" + date +
                ", quantity=" + quantity +
                ", amount=" + amount +
                '}';
    }
}

