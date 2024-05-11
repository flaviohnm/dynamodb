package br.com.dynamodb.service.impl;

import br.com.dynamodb.converter.Converter;
import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;
import br.com.dynamodb.repository.CostumerRepository;
import br.com.dynamodb.service.CostumerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CostumerServiceImpl implements CostumerService {

    private final CostumerRepository repository;

    public Converter converter = new Converter();

    public CostumerServiceImpl(CostumerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CostumerDTO saveCostumer(CostumerDTO costumerDTO) {
        if (repository.findByCompanyDocumentNumber(
                        costumerDTO.getCompanyDocumentNumber())
                .isPresent()) {
            throw new RuntimeException("There is already a customer with this document number");
        }
        var costumer = repository.save(converter.toCostumer(costumerDTO));
        return converter.toCostumerDTO(costumer);
    }

    @Override
    public List<CostumerDTO> findAllCostumers() {

        List<CostumerDTO> allCostumersDTO = new ArrayList<>();

        var costumers = repository.findAll();
        costumers
                .iterator().forEachRemaining(costumer -> {
                    allCostumersDTO.add(converter.toCostumerDTO(costumer));
                });

        return allCostumersDTO;
    }

    @Override
    public List<CostumerDTO> findByCompanyName(String companyName) {

        return converter.toCostumerDTOList(repository.findByCompanyName(companyName));
    }

    @Override
    public CostumerDTO updateCostumer(CostumerDTO costumerDTO) {
        var costumer =
                repository.findByCompanyDocumentNumber(costumerDTO.getCompanyDocumentNumber());

        if (costumer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        costumer.get().setCompanyName(costumerDTO.getCompanyName());
        costumer.get().setPhoneNumber(costumerDTO.getPhoneNumber());

        return converter.toCostumerDTO(repository.save(costumer.get()));
    }

    @Override
    public CostumerDTO disableCostumer(String companyDocumentNumber) {
        Optional<Costumer> costumer =
                repository.findByCompanyDocumentNumber(companyDocumentNumber);

        if (costumer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }
        costumer.get().setActive(false);

        var disableCostumer = repository.save(costumer.get());

        return converter.toCostumerDTO(disableCostumer);
    }

}
