package com.mskara.todoapp.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mskara.todoapp.model.dto.ApiErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Object> handleAuthenticationException(JWTVerificationException exception, WebRequest request) {

        log.error(exception.getMessage());

        ApiErrorResponseDto response = new ApiErrorResponseDto(
                HttpStatus.UNAUTHORIZED,
                "Invalid Token!",
                getRequestPath(request));

        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {

        log.error(exception.getMessage());

        ApiErrorResponseDto response = new ApiErrorResponseDto(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                getRequestPath(request));

        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {

        List<String> details = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorResponseDto response = new ApiErrorResponseDto(
                HttpStatus.BAD_REQUEST,
                "Invalid Parameters!",
                getRequestPath(request));

        response.setDetails(details);

        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @ExceptionHandler({ItemNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException exception, WebRequest request) {

        log.error(exception.getMessage());

        ApiErrorResponseDto response = new ApiErrorResponseDto(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                getRequestPath(request));

        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception, WebRequest request) {

        log.error(exception.getMessage());

        ApiErrorResponseDto response = new ApiErrorResponseDto(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                getRequestPath(request));

        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception, WebRequest request) {

        log.error(exception.getMessage());

        ApiErrorResponseDto response = new ApiErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error Occurred!",
                getRequestPath(request));

        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    private String getRequestPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}

