package com.accelerex.tasks_manager.controller;


import com.accelerex.tasks_manager.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {


        @ExceptionHandler({UserNotFoundException.class})
        public ResponseEntity<?> handleException(UserNotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
}
