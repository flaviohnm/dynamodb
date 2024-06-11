package br.com.dynamodb.controller;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("customer")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity(customerService.saveCustomer(customerDTO), HttpStatus.OK);
    }

    @GetMapping("customer")
    public ResponseEntity<List<CustomerDTO>> findCustomerByName(@Param("companyName") String companyName) {
        return ResponseEntity.ok(customerService.findByCompanyName(companyName));
    }

    @GetMapping("customer/all")
    public ResponseEntity<List<CustomerDTO>> Customers() {
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    @PatchMapping("customer")
    public ResponseEntity<CustomerDTO> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(customerDTO));
    }

    @PatchMapping(value = "customer/{companyDocumentNumber}")
    public ResponseEntity<CustomerDTO> disableCustomer(@PathVariable(value = "companyDocumentNumber") String companyDocumentNumber) {
        return ResponseEntity.ok(customerService.disableCustomer(companyDocumentNumber));
    }

}