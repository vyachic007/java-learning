package by.slava_borisov.hoteladmin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AmenityDto(
        Long id,

        @NotBlank(message = "Название услуги обязательно")
        String name,

        @NotNull(message = "Цена услуги обязательна")
        @DecimalMin(value = "0.0", inclusive = false, message = "Цена услуги должна быть больше 0")
        BigDecimal price,

        @NotBlank(message = "Категория услуги обязательна")
        String category
) { }