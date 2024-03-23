package com.onedayoffer.taskdistribution.controllers.errors;

import com.onedayoffer.taskdistribution.DTO.ErrorMessageDTO;
import com.onedayoffer.taskdistribution.exceptions.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global error handler.
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    /**
     * ValidationException handler.
     * @param ex Occurred ValidationException.
     * @param request Failed request.
     * @return ErrorMessageDTO.
     */
    @ExceptionHandler({ ValidationException.class })
    protected ResponseEntity<Object> handleBadRequest(final ValidationException ex, final WebRequest request) {
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(ex.getMessage());
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * EntityNotFoundException handler.
     * @param ex Occurred EntityNotFoundException.
     * @param request Failed request.
     * @return ErrorMessageDTO.
     */
    @ExceptionHandler({ EntityNotFoundException.class })
    protected ResponseEntity<Object> handleBadRequest(final EntityNotFoundException ex, final WebRequest request) {
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(ex.getMessage());
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Anu other exception handler.
     * @param ex Non-handled exception.
     * @param request Failed request.
     * @return ErrorMessageDTO.
     */
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<Object> handleInternalServerError(final Exception ex, final WebRequest request) {
        ErrorMessageDTO errorMessage = new ErrorMessageDTO("Unexpected error occurred");
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
