package br.com.dynamodb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class CostumerDTO implements Serializable {

    private static final long serialVersionUID = 6318931228062100559L;

    @JsonProperty("companyName")
    @NotNull
    @NotBlank
    private String companyName;

    @JsonProperty("companyDocumentNumber")
    @NotNull
    @NotBlank
    private String companyDocumentNumber;

    @JsonProperty("phoneNumber")
    @NotNull
    @NotBlank
    private String phoneNumber;

    @JsonProperty("createDate")
    private String createDate;

    @JsonProperty("expirationDate")
    private String expirationDate;

    @JsonProperty("active")
    private Boolean active;

    public CostumerDTO(String companyName, String companyDocumentNumber, String phoneNumber) {
        this.companyName = companyName;
        this.companyDocumentNumber = companyDocumentNumber;
        this.phoneNumber = phoneNumber;
    }

    public CostumerDTO() {
    }

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



}