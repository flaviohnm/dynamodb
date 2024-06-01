package br.com.dynamodb.repository;

import br.com.dynamodb.model.Customer;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface CustomerRepository extends CrudRepository<Customer, String> {
    List<Customer> findByCompanyName(String companyName);
    Optional<Customer> findByCompanyDocumentNumber(String companyDocumentNumber);
}
