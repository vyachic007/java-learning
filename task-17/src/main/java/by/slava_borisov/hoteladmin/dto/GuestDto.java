package by.slava_borisov.hoteladmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GuestDto(
        Long id,

        @NotBlank(message = "ФИО гостя обязательно")
        String fullName,

        @NotBlank(message = "Телефон гостя обязателен")
        @Pattern(
                regexp = "^\\+?[0-9()\\-\\s]{7,20}$",
                message = "Некорректный формат телефона"
        )
        String phone
) { }