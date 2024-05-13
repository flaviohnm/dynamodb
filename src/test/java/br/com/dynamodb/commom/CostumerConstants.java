package br.com.dynamodb.commom;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;

public class CostumerConstants {

    public static final Costumer COSTUMER_ID = new Costumer(
            "c630b6d5-8650-4bcb-89a2-61e0500fcb95",
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-03T20:14:39.387192300",
            1L,
            true
    );

    public static final Costumer COSTUMER = new Costumer(
            "Empresa Portuguesa LTDA",
            "6598752300011",
            "11-44444-55511",
            "2024-05-03T20:14:39.387192300",
            1L,
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


}
