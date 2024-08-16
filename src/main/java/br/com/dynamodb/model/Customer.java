package br.com.dynamodb.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serial;
import java.io.Serializable;

@Data
@Setter
@DynamoDbBean
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = -4282005207341771716L;
    @JsonIgnore
    private String id;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("company_document_number")
    private String companyDocumentNumber;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("create_date")
    private String createDate;
    @JsonProperty("updated_date")
    private String updatedDate;
    @JsonProperty("expiration_date")
    private Long expirationDate;
    @JsonProperty("active")
    private Boolean active;

    public Customer() {
    }

    public Customer(String id, String companyName, String companyDocumentNumber, String phoneNumber, String createDate, String updatedDate, Long expirationDate, Boolean active) {
        this.id = id;
        this.companyName = companyName;
        this.companyDocumentNumber = companyDocumentNumber;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.updatedDate = updatedDate;
        this.expirationDate = expirationDate;
        this.active = active;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("company_name")
    public String getCompanyName() {
        return companyName;
    }

    @DynamoDbAttribute("company_document_number")
    public String getCompanyDocumentNumber() {
        return companyDocumentNumber;
    }

    @DynamoDbAttribute("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @DynamoDbAttribute("create_date")
    public String getCreateDate() {
        return createDate;
    }


    @DynamoDbAttribute("updated_date")
    public String getUpdatedDate() {
        return updatedDate;
    }


    @DynamoDbAttribute("expiration_date")
    public Long getExpirationDate() {
        return expirationDate;
    }


    @DynamoDbAttribute("active")
    public Boolean getActive() {
        return active;
    }

}