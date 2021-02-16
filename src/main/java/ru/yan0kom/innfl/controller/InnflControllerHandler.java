package ru.yan0kom.innfl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yan0kom.innfl.error.PersonNotFound;

@RestControllerAdvice(basePackageClasses = ru.yan0kom.innfl.controller.InnflController.class)
public class InnflControllerHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(PersonNotFound.class)
    public ResponseEntity<String> handleNotFound(PersonNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
