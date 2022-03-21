package com.yekh.identity.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class APIError {
  private String error;
  private String error_description;
}
