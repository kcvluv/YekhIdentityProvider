package com.yekh.identity.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class DigestUtilTester {
  private static final Logger logger = LoggerFactory.getLogger(DigestUtilTester.class);

  public static void main(String[] args) {
    String sha256hexCodeVerifier = DigestUtils.sha256Hex("sfasdfwr3445");
    String encodedURLStr = Base64.getUrlEncoder()
      .encodeToString(sha256hexCodeVerifier.getBytes());

    logger.info("encodedURLStr ::{}", encodedURLStr);
  }
}
