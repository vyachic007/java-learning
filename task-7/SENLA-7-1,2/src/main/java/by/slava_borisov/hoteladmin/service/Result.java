package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.util.Messages;

public class Result<T> {
    private boolean isSuccess;
    private T data;
    private String errorMessage;

    private Result(boolean isSuccess, T data, String errorMessage) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(false, null, errorMessage);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public T getData() {
        return data;
    }

    public T getOrNull() {
        return isSuccess ? data : null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        if (isSuccess) {
            return Messages.SUCCESS_OPERATION;
        } else {
            return errorMessage != null ? errorMessage : Messages.ERROR_PREFIX + Messages.DEFAULT_ERROR_MESSAGE;
        }
    }

}
