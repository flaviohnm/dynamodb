package br.com.dynamodb.commom;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;

import java.util.ArrayList;
import java.util.List;

public class CostumerConstants {

    public static final Costumer COSTUMER_ID = new Costumer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb95",
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-14T16:26:30.024057900",
            1723663590L,
            true
    );

    public static final Costumer COSTUMER = new Costumer(
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-14T16:26:30.024057900",
            1723663590L,
            true
    );

    public static final Costumer INVALID_COSTUMER = new Costumer(
            "",
            "",
            "",
            "",
            1L,
            false
    );

    public static final CostumerDTO COSTUMER_DTO = new CostumerDTO(
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511"
    );

    public static final CostumerDTO INVALID_COSTUMER_DTO = new CostumerDTO(
            "",
            "",
            ""
    );

    public static final Costumer AMERICANA = new Costumer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb96",
            "Empresa Americana LTDA",
            "6598752300012",
            "12-44444-55512",
            "2024-05-14T16:26:30.024057900",
            1723663590L,
            true
    );

    public static final Costumer CHINESA = new Costumer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb97",
            "Empresa Chinesa LTDA",
            "6598752300013",
            "13-44444-55513",
            "2024-05-14T16:26:30.024057900",
            1723663590L,
            true
    );

    public static final Costumer BRASILEIRA = new Costumer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb98",
            "Empresa Brasileira LTDA",
            "6598752300014",
            "14-44444-55514",
            "2024-05-14T16:26:30.024057900",
            1723663590L,
            true
    );


    public static final List<Costumer> COSTUMERS = new ArrayList<>(){
        {
            add(AMERICANA);
            add(CHINESA);
            add(BRASILEIRA);
        }
    };


}
