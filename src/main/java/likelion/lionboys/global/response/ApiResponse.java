package likelion.lionboys.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import likelion.lionboys.global.exception.error.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(String code, String message, T data) {

    public static final String SUCCESS_CODE = "OK";

    /* ---------- 성공 ---------- */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(SUCCESS_CODE, null, null);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(SUCCESS_CODE, message, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_CODE, null, data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(SUCCESS_CODE, message, data);
    }

    /* ---------- 실패 ---------- */
    public static <T> ApiResponse<T> fail(ErrorCode error) {
        return new ApiResponse<>(error.getCode(), error.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode error, String message) {
        return new ApiResponse<>(error.getCode(), message, null);
    }
}