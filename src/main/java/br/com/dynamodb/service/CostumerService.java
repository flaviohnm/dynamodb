package br.com.dynamodb.service;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;

import java.util.List;

public interface CostumerService {
    CostumerDTO saveCostumer(CostumerDTO costumerDTO);
    List<CostumerDTO> findAllCostumers();
    List<CostumerDTO> findByCompanyName(String companyName);
    CostumerDTO updateCostumer(CostumerDTO costumerDTO);
    CostumerDTO disableCostumer(String companyDocumentNumber);
}