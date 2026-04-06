package by.slava_borisov.hoteladmin.dto.response;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) { }
