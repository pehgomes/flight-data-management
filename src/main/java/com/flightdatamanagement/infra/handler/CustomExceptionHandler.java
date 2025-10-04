package com.flightdatamanagement.infra.handler;

import com.flightdatamanagement.infra.exception.BusinessException;
import com.flightdatamanagement.infra.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> argumentNotValidException(
            MethodArgumentNotValidException ex) {

        List<String> errors =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();

        return new ResponseEntity<>(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MissingServletRequestParameterException ex) {

        return new ResponseEntity<>(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(List.of(ex.getParameterName() + " " + ex.getMessage())).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleVConstraintValidationException(
            ConstraintViolationException ex) {

        List<String> errors =
                ex.getConstraintViolations().stream()
                        .map(c -> c.getPropertyPath().toString() + " - " + c.getMessage())
                        .toList();

        return new ResponseEntity<>(
                ApiError.builder().status(HttpStatus.BAD_REQUEST.value()).errors(errors).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        final var apiError =
                ApiError.builder()
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .errors(List.of(ex.getMessage())).build();

        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        final var apiError =
                ApiError.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .errors(List.of(ex.getMessage())).build();

        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
