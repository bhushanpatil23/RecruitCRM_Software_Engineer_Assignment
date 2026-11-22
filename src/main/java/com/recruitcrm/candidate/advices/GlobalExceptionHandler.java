package com.recruitcrm.candidate.advices;

import com.recruitcrm.candidate.exceptions.BadRequestException;
import com.recruitcrm.candidate.exceptions.ResourceNotFoundException;
import com.recruitcrm.candidate.exceptions.DuplicateCandidateException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(DuplicateCandidateException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateCandidate(DuplicateCandidateException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputValidationErrors(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input validation failed")
                .subErrors(errors)
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<?>> handleBindingErrors(BindException exception) {
        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input validation failed")
                .subErrors(errors)
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input validation failed")
                .subErrors(errors)
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Malformed JSON or invalid request body value")
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingParameter(MissingServletRequestParameterException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Missing required request parameter: " + exception.getParameterName())
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid value '" + exception.getValue()
                        + "' for parameter '" + exception.getName() + "'")
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message("The request conflicts with existing data")
                .build();
        return buildErrorResponse(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .build();
        return buildErrorResponse(apiError);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponse(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getStatus());
    }
}
