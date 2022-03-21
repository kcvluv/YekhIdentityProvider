package com.yekh.identity.bean;

import lombok.Data;

@Data
public class TokenPayload {
  private String code;
  private String code_verifier;
  private String grant_type;
  private String redirect_uri;
}
