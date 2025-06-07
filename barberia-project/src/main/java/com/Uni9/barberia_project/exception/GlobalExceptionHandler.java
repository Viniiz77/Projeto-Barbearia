package com.Uni9.barberia_project.exception;

import com.Uni9.barberia_project.dto.ErrorMessageDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Esse método vai lidar com a exceção EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorMessageDto error = new ErrorMessageDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        //Retorna um status http 404
        //E no corpo da resposta, a mensagem que veio da exceção
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessageDto error = new ErrorMessageDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
