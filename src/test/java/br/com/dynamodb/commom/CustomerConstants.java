package br.com.dynamodb.commom;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerConstants {

    public static final Customer CREATED_CUSTOMER_ID = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb95",
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-14T16:26:30.024057900",
            null,
            1723663590L,
            true
    );

    public static final Customer CUSTOMER_ID = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb95",
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-14T16:26:30.024057900",
            "2024-06-10T13:00:00.000",
            1723663590L,
            true
    );

    public static final Customer DISABLE_CUSTOMER_ID = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb95",
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-14T16:26:30.024057900",
            "2024-05-14T16:26:30.024057900",
            1723663590L,
            false
    );

    public static final CustomerDTO CUSTOMER_DTO = new CustomerDTO(
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511"
    );

    public static final CustomerDTO INVALID_COSTUMER_DTO = new CustomerDTO(
            "",
            "",
            ""
    );

    public static final Customer AMERICANA = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb96",
            "Empresa Americana LTDA",
            "6598752300012",
            "12-44444-55512",
            "2024-05-14T16:26:30.024057900",
            "2024-06-10T13:00:00.000",
            1723663590L,
            true
    );

    public static final Customer CHINESA = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb97",
            "Empresa Chinesa LTDA",
            "6598752300013",
            "13-44444-55513",
            "2024-05-14T16:26:30.024057900",
            "2024-06-10T13:00:00.000",
            1723663590L,
            true
    );

    public static final Customer BRASILEIRA = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb98",
            "Empresa Brasileira LTDA",
            "6598752300014",
            "14-44444-55514",
            "2024-05-14T16:26:30.024057900",
            "2024-06-10T13:00:00.000",
            1723663590L,
            true
    );

    public static final Customer CANADENSE = new Customer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb99",
            "Empresa Canadense LTDA",
            "6598752300015",
            "14-44444-55514",
            "2024-05-14T16:26:30.024057900",
            null,
            1723663590L,
            true
    );



    public static final List<Customer> CUSTOMERS = new ArrayList<>() {
        {
            add(AMERICANA);
            add(CHINESA);
            add(CANADENSE);
            add(BRASILEIRA);
        }
    };

}
