package br.com.dynamodb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonProperty("createDate")
    private String createDate;

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

}