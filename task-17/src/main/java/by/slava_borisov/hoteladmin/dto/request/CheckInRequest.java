package by.slava_borisov.hoteladmin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CheckInRequest(
        @NotNull(message = "ID гостя обязателен")
        @Positive(message = "ID гостя должен быть положительным")
        Long guestId,

        @NotNull(message = "ID комнаты обязателен")
        @Positive(message = "ID комнаты должен быть положительным")
        Long roomId,

        @NotNull(message = "Дата заселения обязательна")
        LocalDate checkInDate,

        @NotNull(message = "Дата выселения обязательна")
        LocalDate checkOutDate
) { }