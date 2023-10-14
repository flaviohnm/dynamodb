package br.com.dynamodb.service;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;

import java.util.List;

public interface CostumerService {
    Costumer saveCostumer(CostumerDTO costumerDTO);
    List<Costumer> findAllCostumers();
    List<Costumer> findByCompanyName(String companyName);
    Costumer updateCostumer(String companyDocumentNumber, CostumerDTO costumerDTO);
    Costumer disableCostumer(String companyDocumentNumber);
}