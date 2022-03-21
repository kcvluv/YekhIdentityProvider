package com.yekh.identity.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

  private String code;

  public BadRequestException(String code, String message) {
    super(message);
    this.code = code;
  }

  public BadRequestException(String code, String message, Throwable throwable) {
    super(message, throwable);
    this.code = code;
  }
}
