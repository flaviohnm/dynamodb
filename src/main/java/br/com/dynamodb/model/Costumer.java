package br.com.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@DynamoDBTable(tableName = "costumer")
public class Costumer implements Serializable {

    private static final long serialVersionUID = -4282005207341771716L;


    @JsonIgnore
    private String id;


    @JsonProperty("company_name")
    private String companyName;


    @JsonProperty("company_document_number")
    private String companyDocumentNumber;


    @JsonProperty("phone_number")
    private String phoneNumber;


    @JsonProperty("active")
    private Boolean active;

    public Costumer() {
    }

    public Costumer(
            String companyName,
            String companyDocumentNumber,
            String phoneNumber,
            Boolean active
    ) {
        this.companyName = companyName;
        this.companyDocumentNumber = companyDocumentNumber;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @DynamoDBAttribute(attributeName = "company_document_number")
    public String getCompanyDocumentNumber() {
        return companyDocumentNumber;
    }

    public void setCompanyDocumentNumber(String companyDocumentNumber) {
        this.companyDocumentNumber = companyDocumentNumber;
    }

    @DynamoDBAttribute(attributeName = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @DynamoDBAttribute(attributeName = "active")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
