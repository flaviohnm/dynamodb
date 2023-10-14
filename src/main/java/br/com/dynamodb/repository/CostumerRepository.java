package br.com.dynamodb.repository;

import br.com.dynamodb.model.Costumer;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface CostumerRepository extends CrudRepository<Costumer, String> {
    List<Costumer> findByCompanyName(String companyName);
    Optional<Costumer> findByCompanyDocumentNumber(String companyDocumentNumber);
}
