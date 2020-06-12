package com.psp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AuthorityExceptionHandler {

    @ExceptionHandler(value={LoginFailedException.class})
    public ResponseEntity<Object> handleAuthorityException(LoginFailedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiException loginFailed = new ApiException(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(loginFailed, status);
    }
}
