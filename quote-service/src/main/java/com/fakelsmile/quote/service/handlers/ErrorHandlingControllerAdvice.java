package com.fakelsmile.quote.service.handlers;

import com.fakelsmile.quote.service.errors.AlreadyExistException;
import com.fakelsmile.quote.service.errors.NotFoundException;
import com.fakelsmile.quote.service.models.dto.ErrorDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Handle the exceptions and return custom error responses.
 */
@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    /**
     * Handle and return list of errors in the response body.
     *
     * @param e - error to handle
     * @return list of errors
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDTO> handleConstraintValidationException(ConstraintViolationException e) {
        log.debug("Validation error", e);
        return e.getConstraintViolations().stream()
                .map(
                        error -> new ErrorDTO(
                                error.getPropertyPath().toString(),
                                error.getMessage()
                        )
                )
                .toList();
    }

    /**
     * Handle and return list of errors in the response body.
     *
     * @param e - error to handle
     * @return list of errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("Validation error", e);
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();
    }

    /**
     * Handle and return error in the response body.
     *
     * @param e - error to handle
     * @return error
     */
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException e) {
        log.debug("Not found error", e);
        return new ErrorDTO(null, e.getMessage());
    }

    /**
     * Handle and return error in the response body.
     *
     * @param e - error to handle
     * @return error
     */
    @ResponseBody
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleAlreadyExistException(AlreadyExistException e) {
        log.debug("Already exist error", e);
        return new ErrorDTO(null, e.getMessage());
    }
}
