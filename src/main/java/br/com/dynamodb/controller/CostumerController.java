package br.com.dynamodb.controller;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.service.CostumerService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/")
public class CostumerController {

    private final CostumerService costumerService;

    public CostumerController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @PostMapping("costumer")
    public ResponseEntity<CostumerDTO> createCostumer(@Valid @RequestBody CostumerDTO costumerDTO) {
        return new ResponseEntity(costumerService.saveCostumer(costumerDTO), HttpStatus.OK);
    }

    @GetMapping("costumer")
    public ResponseEntity<List<CostumerDTO>> findCostumerByName(@Param("companyName") String companyName) {
        return ResponseEntity.ok(costumerService.findByCompanyName(companyName));
    }

    @GetMapping("costumer/all")
    public ResponseEntity<List<CostumerDTO>> allCostumers() {
        return ResponseEntity.ok(costumerService.findAllCostumers());
    }

    @PutMapping("costumer")
    public ResponseEntity<CostumerDTO> updateCostumer(@Valid @RequestBody CostumerDTO costumerDTO) {
        return ResponseEntity.ok(costumerService.updateCostumer(costumerDTO));
    }

    @PatchMapping(value = "costumer/{companyDocumentNumber}")
    public ResponseEntity<CostumerDTO> disableCostumer(@PathVariable(value = "companyDocumentNumber") String companyDocumentNumber) {
        return ResponseEntity.ok(costumerService.disableCostumer(companyDocumentNumber));
    }

}