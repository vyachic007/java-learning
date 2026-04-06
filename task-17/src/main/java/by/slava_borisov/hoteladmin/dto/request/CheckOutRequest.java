package by.slava_borisov.hoteladmin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CheckOutRequest(
        @NotNull(message = "ID комнаты обязателен")
        @Positive(message = "ID комнаты должен быть положительным")
        Long roomId
) { }