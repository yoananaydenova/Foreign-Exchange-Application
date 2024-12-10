package com.yoanan.foreignexchangeapp.exceptions;

import com.yoanan.foreignexchangeapp.model.view.ErrorMessage;
import jakarta.validation.ConstraintViolationException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    // handle validation errors
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, String> body = new HashMap<>();
        body.put("error", String.join(" ", errors));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    // empty page
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handler(Exception ex, WebRequest request) {
        String errorMessageDescription = ex.getLocalizedMessage();
        if (errorMessageDescription == null) {
            errorMessageDescription = ex.toString();
        }
        ErrorMessage errorMessage = new ErrorMessage(errorMessageDescription);

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        String errorMessageDescription = ex.getLocalizedMessage();

        if (errorMessageDescription == null) {
            errorMessageDescription = ex.toString();
        }
        ErrorMessage errorMessage = new ErrorMessage(errorMessageDescription);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ExchangeRateClientFailureException.class})
    public ResponseEntity<Object> handleExchangeRateClientFailureException(Exception ex, WebRequest request) {

        String errorMessageDescription = "Invalid base and/or quote name of currency!";

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<Object> handleInvalidCurrency(InvalidCurrencyException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleSpecificExceptions(Exception ex, WebRequest request) {

        String errorMessageDescription = ex.getLocalizedMessage();
        if (errorMessageDescription == null) {
            errorMessageDescription = ex.toString();
        }

        ErrorMessage errorMessage = new ErrorMessage(errorMessageDescription);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {

        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(m -> {
                    String message = m.getMessage();
                    return message.substring(0, 1).toUpperCase() + message.substring(1) + "!";
                }).collect(Collectors.toList());

        Map<String, String> body = new HashMap<>();
        body.put("error", String.join(" ", errors));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
