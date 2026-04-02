package by.slava_borisov.hoteladmin.dto;

import jakarta.validation.constraints.NotBlank;

public record GuestDto(
        Long id,

        @NotBlank(message = "ФИО гостя обязательно")
        String fullName,

        @NotBlank(message = "Телефон гостя обязателен")
        String phone
) { }