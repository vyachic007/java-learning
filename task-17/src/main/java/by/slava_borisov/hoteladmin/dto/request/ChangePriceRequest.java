package by.slava_borisov.hoteladmin.dto.request;

import java.math.BigDecimal;

public record ChangePriceRequest(
        BigDecimal newPrice
) { }
