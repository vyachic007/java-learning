package by.slava_borisov.hoteladmin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RoomDto(
        Long id,

        @NotNull(message = "Номер комнаты обязателен")
        @Positive(message = "Номер комнаты должен быть положительным")
        Integer number,

        @NotNull(message = "Цена за ночь обязательна")
        @DecimalMin(value = "0.0", inclusive = false, message = "Цена за ночь должна быть больше 0")
        BigDecimal pricePerNight,

        @NotNull(message = "Статус комнаты обязателен")
        RoomStatusDto status,

        @Min(value = 1, message = "Вместимость должна быть не меньше 1")
        int capacity,

        @Min(value = 1, message = "Количество звезд должно быть не меньше 1")
        @Max(value = 5, message = "Количество звезд должно быть не больше 5")
        int stars
) { }