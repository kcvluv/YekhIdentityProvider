package com.yekh.identity.util;

import org.apache.commons.lang3.RandomStringUtils;

public class YekhOneIdentityAuthUtil {

  public static String getAuthCode() {
    int length = 10;
    boolean useLetters = true;
    boolean useNumbers = true;
    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
    return generatedString;
  }
}
