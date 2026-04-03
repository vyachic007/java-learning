package by.slava_borisov.hoteladmin.model.value;

import java.util.Objects;

public class PhoneNumber {

    private static final String PHONE_REGEX = "^\\+?[0-9()\\-\\s]{7,20}$";

    private final String value;

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Телефон обязателен");
        }

        String normalized = value.trim();

        if (!normalized.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Некорректный формат телефона");
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}