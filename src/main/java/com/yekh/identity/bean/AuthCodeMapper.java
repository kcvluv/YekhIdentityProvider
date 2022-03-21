package com.yekh.identity.bean;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthCodeMapper {
  static Map<String, String> authCodeMap = new HashMap<>();

  public void addAuthCode(String authCode, String codeChallenge) {
    authCodeMap.put(authCode, codeChallenge);
  }

  public void invalidateAuthCode(String authCode) {
    authCodeMap.remove(authCode);
  }

  public String getCodeChallenge(String authCode) {
    return authCodeMap.get(authCode);
  }
}
