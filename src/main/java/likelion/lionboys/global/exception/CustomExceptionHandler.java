package likelion.lionboys.global.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import likelion.lionboys.global.exception.error.ErrorCode;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import likelion.lionboys.domain.party.exception.error.PartyErrorCode;
import likelion.lionboys.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    /* ---------- 도메인/비즈니스 예외 ---------- */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(CustomException ex, HttpServletRequest req) {
        ErrorCode ec = ex.getErrorCode();
        String msg = (ex.getMessage() != null) ? ex.getMessage() : ec.getMessage();
        logAt(ec.getStatus(), "BizError [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), msg);
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ec, msg));
    }

    /* ---------- 검증 에러(@Valid - Body) ---------- */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                       HttpServletRequest req) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ":" + f.getDefaultMessage())
                .toList();
        var ec = GlobalErrorCode.BAD_REQUEST;
        log.warn("Validation [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), fieldErrors);
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ec, String.join(", ", fieldErrors)));
    }

    /* ---------- 검증 에러(@Validated - Param/Path) ---------- */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex,
                                                                    HttpServletRequest req) {
        var messages = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ":" + v.getMessage())
                .toList();
        var ec = GlobalErrorCode.BAD_REQUEST;
        log.warn("Constraint [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), messages);
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ec, String.join(", ", messages)));
    }

    /* ---------- 타입/형식 오류 ---------- */
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            ServletRequestBindingException.class
    })
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(Exception ex, HttpServletRequest req) {
        var ec = GlobalErrorCode.BAD_REQUEST;
        log.warn("BadRequest [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ec, "요청 값을 확인해 주세요."));
    }

    /* ---------- 데이터 무결성(Unique) ---------- */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        String raw = (ex.getMostSpecificCause() != null) ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String m = (raw == null) ? "" : raw.toLowerCase();

        ErrorCode ec =
            (m.contains("uk_party_phone"))  ? PartyErrorCode.PARTICIPANT_DUPLICATE :
            (m.contains("uk_round_seq"))    ? PartyErrorCode.ROUND_SEQ_CONFLICT    :
            (m.contains("uk_checkin_once")) ? PartyErrorCode.CHECKIN_DUPLICATE     :
            GlobalErrorCode.CONFLICT;

        logAt(ec.getStatus(), "Integrity [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), raw);
        return ResponseEntity.status(ec.getStatus()).body(ApiResponse.fail(ec));
    }

    /* ---------- Not Found (404: 경로/정적리소스) ---------- */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<ApiResponse<?>> handleNotFound404(Exception ex, HttpServletRequest req) {
        var ec = GlobalErrorCode.NOT_FOUND;
        log.warn("NotFound [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ec.getStatus()).body(ApiResponse.fail(ec));
    }

    /* ---------- 405: 메서드 미지원 ---------- */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                                 HttpServletRequest req) {
        var ec = GlobalErrorCode.NOT_SUPPORTED_METHOD;
        log.warn("MethodNotAllowed [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ec.getStatus()).body(ApiResponse.fail(ec));
    }

    /* ---------- 415: 미디어 타입 미지원 ---------- */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex,
                                                                     HttpServletRequest req) {
        var ec = GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE;
        log.warn("UnsupportedMediaType [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ec.getStatus()).body(ApiResponse.fail(ec));
    }

    /* ---------- 엔티티 Not Found (도메인 조회 실패) ---------- */
    @ExceptionHandler({
            ChangeSetPersister.NotFoundException.class,
            EntityNotFoundException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<ApiResponse<?>> handleEntityNotFound(Exception ex, HttpServletRequest req) {
        var ec = GlobalErrorCode.NOT_FOUND;
        log.warn("EntityNotFound [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ec.getStatus()).body(ApiResponse.fail(ec));
    }

    /* ---------- 최종 Fallback ---------- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex, HttpServletRequest req) {
        var ec = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Unhandled [{}] {} {} ({}) - {}", ec.getCode(), req.getMethod(), req.getRequestURI(),
                ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.fail(ec, "서버 오류가 발생했습니다."));
    }

    /* ---------- 공통 로깅 레벨 ---------- */
    private void logAt(HttpStatus status, String fmt, Object... args) {
        if (status.is5xxServerError()) log.error(fmt, args);
        else log.warn(fmt, args);
    }
}


