package com.yekh.identity.bean;

import com.yekh.identity.util.Scopes;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class OpenIdConfiguration {
  private String issuer;
  private String authorization_endpoint;
  private String token_endpoint;
  private String jwks_uri;
  private List<Scopes> scopes_supported = Arrays.asList(Scopes.values());
}
