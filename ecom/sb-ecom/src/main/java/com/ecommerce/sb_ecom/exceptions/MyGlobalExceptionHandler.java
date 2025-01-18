package com.ecommerce.sb_ecom.exceptions;

import com.ecommerce.sb_ecom.payload.APIResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice

public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class )
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> response=new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(
                err->{
                    String fieldName=((FieldError)err).getField();
                    String message= err.getDefaultMessage();
                    response.put(fieldName,message);
                }
        );
      return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myMethodResourceNotFoundException(ResourceNotFoundException e){
        APIResponse apiResponse=new APIResponse(e.getMessage(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myMethodAPIException(APIException e){
        APIResponse apiResponse=new APIResponse(e.getMessage(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

}
