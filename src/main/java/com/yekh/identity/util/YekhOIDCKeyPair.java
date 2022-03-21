package com.yekh.identity.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.yekh.identity.controller.YekhOneIdentityController;
import lombok.Getter;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
@Getter
public class YekhOIDCKeyPair {
  private static final Logger logger = LoggerFactory.getLogger(YekhOneIdentityController.class);

  private JWKSet publicKeySet;
  private JSONObject publicKeyObject;

  private JWKSet privateKeySet;

  @Autowired
  public YekhOIDCKeyPair() {
    //Loading the public key from resource file.
    loadPublicKeys();
    loadPublicKeyAsJsonKeyObject();

    //Loading the private key from resource file.
    loadPrivateKeys();
  }

  private void loadPublicKeyAsJsonKeyObject() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      publicKeyObject = objectMapper.readValue(classLoader.getResourceAsStream("/yek-identity-privatekey.json"), JSONObject.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadPublicKeys() {
    logger.info("loading the public key from file");
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      //File file = new File(classLoader.getResource("yek-identity-publickey.json").getFile());
      publicKeySet = JWKSet.load(classLoader.getResourceAsStream("/yek-identity-publickey.json"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private void loadPrivateKeys() {
    logger.info("loading the public key from file");
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      // File file = new File(classLoader.getResource("yek-identity-privatekey.json").getFile());
      privateKeySet = JWKSet.load(classLoader.getResourceAsStream("/yek-identity-privatekey.json"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

}
