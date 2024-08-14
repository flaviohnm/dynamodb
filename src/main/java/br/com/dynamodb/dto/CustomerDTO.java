package br.com.dynamodb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

@Builder
@AllArgsConstructor
public class CustomerDTO implements Serializable {

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

    @NotNull
    @NotBlank
    @JsonProperty("createDate")
    private String createDate;

    @NotNull
    @NotBlank
    @JsonProperty("expirationDate")
    private String expirationDate;

    @JsonProperty("updatedDate")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedDate;

    @JsonProperty("active")
    private Boolean active;

    public CustomerDTO(String companyName, String companyDocumentNumber, String phoneNumber) {
        this.companyName = companyName;
        this.companyDocumentNumber = companyDocumentNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyDocumentNumber() {
        return companyDocumentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public Boolean getActive() {
        return active;
    }


}