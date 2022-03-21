package com.yekh.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
public class APIGlobalExceptionHandler
  extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequest(BadRequestException ex,
                                                 WebRequest request) {

    return new ResponseEntity(getErrorObject("invalid_request", ex.getMessage(), request), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstrainViolation(ConstraintViolationException ex,
                                                         WebRequest request) {
    return new ResponseEntity(getErrorObject("invalid_request", ex.getMessage(), request), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(UpstreamSystemFailedException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ResponseEntity<Object> handleUpstreamSystemFailed(UpstreamSystemFailedException ex,
                                                           WebRequest request) {

    return new ResponseEntity(getErrorObject(ex.getCode(), ex.getMessage(), request), HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ResponseEntity<Object> defaultExceptionOccured(Exception ex,
                                                        WebRequest request) {
    return new ResponseEntity(getErrorObject("runtime_error", ex.getMessage(), request), HttpStatus.SERVICE_UNAVAILABLE);
  }


  private Object getErrorObject(String code, String message, WebRequest request) {
    APIError error = new APIError();
    error.setError(code);
    error.setError_description(message);
    return error;

  }
}
