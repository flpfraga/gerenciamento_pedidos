package fraga.com.example.gerenciamento_pedido.exception.handler;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import fraga.com.example.gerenciamento_pedido.exception.*;
import fraga.com.example.gerenciamento_pedido.exception.models.ErrorResponse;
import fraga.com.example.gerenciamento_pedido.exception.models.records.ErrorField;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NotFoundException.class,
            BusinessException.class,
            ConflictException.class,
            UnauthorizedException.class
    })
    public ResponseEntity<ErrorResponse> handleApplicationExceptions(ApplicationException ex, WebRequest request) {
        log.warn("Erro tratado: {}", ex.getMessage());
        return buildErrorResponse(ex.getErro().getMensagem(), ex.getErro().getStatusCode(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorField> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorField(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        String errorMessage = "Erro de validação nos seguintes campos";
        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, request, fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<ErrorField> violations = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorField(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        String errorMessage = "Erro de validação de dados";
        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, request, violations);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormatEx && invalidFormatEx.getTargetType().isEnum()) {
            List<String> allowedValues = Stream.of(invalidFormatEx.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .toList();

            String errorMessage = "Valor inválido: '" + invalidFormatEx.getValue() +
                    "'. Os valores permitidos são: " + allowedValues;
            return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, request);
        }

        return buildErrorResponse("Erro ao ler a requisição. Verifique o formato do JSON.", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Erro interno inesperado: ", ex);
        return buildErrorResponse("Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        return buildErrorResponse(message, status, request, null);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request, List<ErrorField> fields) {
        ErrorResponse.BodyErro body = new ErrorResponse.BodyErro(
                LocalDateTime.now(),
                message,
                request.getDescription(false),
                fields
        );

        return new ResponseEntity<>(new ErrorResponse(body, status.value()), status);
    }
}
