package br.com.dynamodb.dto;

import br.com.dynamodb.converter.Converter;
import br.com.dynamodb.model.Costumer;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CostumerDTO implements Serializable {

    private static final long serialVersionUID = 6318931228062100559L;

    @JsonProperty("company_name")
    @NotNull
    @NotBlank
    private String companyName;

    @JsonProperty("company_document_number")
    @NotNull
    @NotBlank
    private String companyDocumentNumber;

    @JsonProperty("phone_number")
    @NotNull
    @NotBlank
    private String phoneNumber;

    @JsonProperty("create_date")
    private String createDate;

    @JsonProperty("expiration_date")
    private String expirationDate;

    @JsonProperty("active")
    private Boolean active;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDocumentNumber() {
        return companyDocumentNumber;
    }

    public void setCompanyDocumentNumber(String companyDocumentNumber) {
        this.companyDocumentNumber = companyDocumentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Costumer costumerDTOToCostumer() {

        var converter = new Converter();

        return new Costumer(
                this.companyName,
                this.companyDocumentNumber,
                this.phoneNumber,
                this.createDate = LocalDateTime.now().toString(),
                this.expirationDate = converter.toEpocDate(getCreateDate()),
                true
        );
    }

}