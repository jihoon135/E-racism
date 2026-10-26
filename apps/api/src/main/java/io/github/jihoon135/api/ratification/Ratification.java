package io.github.jihoon135.api.ratification;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ratification")
public class Ratification {

    @EmbeddedId
    private RatificationId id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "date_signature")
    private LocalDate dateSignature;

    @Column(name = "date_ratification")
    private LocalDate dateRatification;

    @Column(name = "date_individual_communications")
    private LocalDate dateIndividualCommunications;

    @Column(name = "date_inquiry_procedure")
    private LocalDate dateInquiryProcedure;

    protected Ratification() {
    }

    public RatificationId getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDateSignature() {
        return dateSignature;
    }

    public LocalDate getDateRatification() {
        return dateRatification;
    }

    public LocalDate getDateIndividualCommunications() {
        return dateIndividualCommunications;
    }

    public LocalDate getDateInquiryProcedure() {
        return dateInquiryProcedure;
    }
}
