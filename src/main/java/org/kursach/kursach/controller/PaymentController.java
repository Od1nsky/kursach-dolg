package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Service;
import org.kursach.kursach.service.ClientService;
import org.kursach.kursach.service.PaymentService;
import org.kursach.kursach.service.ServiceService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class PaymentController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private ClientService clientService;
    
    @Inject
    private ServiceService serviceService;
    
    private List<Payment> payments;
    private Payment payment;
    private Long editId;
    private Long selectedClientId;
    private Long selectedServiceId;
    
    @PostConstruct
    public void init() {
        payments = paymentService.getAllPayments();
        
        if (payment == null) {
            payment = new Payment();
            payment.setDate(LocalDate.now());
            logger.info("Создан новый объект Payment");
        } else {
            logger.info("Использован существующий объект Payment с ID: " + payment.getId());
        }
        
        loadPayment();
        
        logger.info("PaymentController инициализирован");
    }
    
    public String save() {
        try {
            if (editId != null && payment.getId() == null) {
                payment.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            // Устанавливаем клиента и услугу по выбранным ID
            if (selectedClientId != null) {
                Client client = clientService.getClientById(selectedClientId);
                payment.setClient(client);
            }
            
            if (selectedServiceId != null) {
                Service service = serviceService.getServiceById(selectedServiceId);
                payment.setService(service);
            }
            
            boolean isNew = (payment.getId() == null);
            logger.info((isNew ? "Создание новой" : "Обновление существующей") + " покупки");
            
            paymentService.savePayment(payment);
            
            editId = null;
            selectedClientId = null;
            selectedServiceId = null;
            payments = paymentService.getAllPayments();
            payment = new Payment();
            payment.setDate(LocalDate.now());
            
            logger.info("Покупка " + (isNew ? "создана" : "обновлена") + " успешно");
            return "payment?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении покупки: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование покупки с ID: " + id);
            payment = paymentService.getPaymentById(id);
            
            if (payment == null) {
                logger.warning("Покупка с ID " + id + " не найдена");
                return "payment?faces-redirect=true";
            }
            
            // Устанавливаем выбранные ID для формы
            if (payment.getClient() != null) {
                selectedClientId = payment.getClient().getId();
            }
            if (payment.getService() != null) {
                selectedServiceId = payment.getService().getId();
            }
            
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            return "payment-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке покупки для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "payment?faces-redirect=true";
        }
    }
    
    public void loadPayment() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка покупки из параметра URL, ID: " + id);
                
                payment = paymentService.getPaymentById(id);
                if (payment == null) {
                    logger.warning("Покупка с ID " + id + " не найдена при загрузке из параметра");
                    payment = new Payment();
                    payment.setDate(LocalDate.now());
                } else {
                    if (payment.getClient() != null) {
                        selectedClientId = payment.getClient().getId();
                    }
                    if (payment.getService() != null) {
                        selectedServiceId = payment.getService().getId();
                    }
                    logger.info("Покупка загружена из параметра URL");
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                payment = new Payment();
                payment.setDate(LocalDate.now());
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление покупки с ID: " + id);
            paymentService.deletePayment(id);
            payments = paymentService.getAllPayments();
            return "payment?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении покупки: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String prepareNew() {
        payment = new Payment();
        payment.setDate(LocalDate.now());
        selectedClientId = null;
        selectedServiceId = null;
        editId = null;
        logger.info("Подготовка новой покупки");
        return "payment-edit?faces-redirect=true";
    }
    
    // Геттеры и сеттеры
    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Long getEditId() {
        return editId;
    }
    
    public void setEditId(Long editId) {
        this.editId = editId;
    }
    
    public Long getSelectedClientId() {
        return selectedClientId;
    }
    
    public void setSelectedClientId(Long selectedClientId) {
        this.selectedClientId = selectedClientId;
    }
    
    public Long getSelectedServiceId() {
        return selectedServiceId;
    }
    
    public void setSelectedServiceId(Long selectedServiceId) {
        this.selectedServiceId = selectedServiceId;
    }
    
    public List<Client> getClients() {
        return clientService.getAllClients();
    }
    
    public List<Service> getServices() {
        return serviceService.getAllServices();
    }
}


