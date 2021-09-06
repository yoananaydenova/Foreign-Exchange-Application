package com.yoanan.foreignexchangeapp.exceptions;

import com.yoanan.foreignexchangeapp.model.view.ErrorMessage;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        System.out.println();
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    // empty page
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handler(Exception ex, WebRequest request) {
        String errorMessageDescription = ex.getLocalizedMessage();

        if(errorMessageDescription==null){
            errorMessageDescription = ex.toString();
        }
        ErrorMessage errorMessage = new ErrorMessage(errorMessageDescription);

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
        System.out.println();
        String errorMessageDescription = ex.getLocalizedMessage();

        if(errorMessageDescription==null){
            errorMessageDescription = ex.toString();
        }
        ErrorMessage errorMessage = new ErrorMessage(errorMessageDescription);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleSpecificExceptions(Exception ex, WebRequest request){

        String errorMessageDescription = ex.getLocalizedMessage();
        if(errorMessageDescription==null){
            errorMessageDescription = ex.toString();
        }
        System.out.println();
        ErrorMessage errorMessage = new ErrorMessage(errorMessageDescription);

        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
