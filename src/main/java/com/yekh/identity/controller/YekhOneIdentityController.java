package com.yekh.identity.controller;

import com.yekh.identity.bean.AuthCodeMapper;
import com.yekh.identity.bean.OpenIdConfiguration;
import com.yekh.identity.bean.TokenPayload;
import com.yekh.identity.bean.YekhIdentityToken;
import com.yekh.identity.exception.BadRequestException;
import com.yekh.identity.exception.RuntimeSystemException;
import com.yekh.identity.exception.UpstreamSystemFailedException;
import com.yekh.identity.service.TokenService;
import com.yekh.identity.util.Scopes;
import com.yekh.identity.util.YekhOIDCKeyPair;
import com.yekh.identity.util.YekhOneIdentityAuthUtil;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class YekhOneIdentityController {
  private static final Logger logger = LoggerFactory.getLogger(YekhOneIdentityController.class);
  @Autowired
  TokenService tokenService;
  @Autowired
  private Environment env;
  @Autowired
  private YekhOIDCKeyPair keySet;
  @Autowired
  private AuthCodeMapper mapper;

  @GetMapping(value = "/.well-known/openid-configuration")
  public ResponseEntity<OpenIdConfiguration> openIdConfiguration() {
    logger.info("Entered into openid configuration");
    OpenIdConfiguration configuration = new OpenIdConfiguration();
    if (env == null) {
      throw new RuntimeSystemException("system_error", "error in loading the environment file");
    }
    try {
      configuration.setIssuer(env.getProperty("openid.issuer"));
      configuration.setAuthorization_endpoint(env.getProperty("openid.authorization_endpoint"));
      configuration.setToken_endpoint(env.getProperty("openid.token_endpoint"));
      configuration.setJwks_uri(env.getProperty("openid.jwks_uri"));
    } finally {
      //Log the app reporting
    }
    logger.info("exiting the openid configuration");
    return new ResponseEntity<>(configuration, HttpStatus.OK);
  }

  @GetMapping(value = "/jwks.json")
  public ResponseEntity<JSONObject> jwtKeySet() {
    logger.info("Entered into jwt keyset method");
    if (keySet == null) {
      throw new UpstreamSystemFailedException("system_error", "failed to load the public key");
    }
    return new ResponseEntity<>(keySet.getPublicKeyObject(), HttpStatus.OK);
  }

  @GetMapping(value = "/authorize")
  public RedirectView authorize(@RequestParam String redirect_uri,
                                @RequestParam String response_type,
                                @RequestParam String response_mode,
                                @RequestParam String code_challenge,
                                @RequestParam String code_challenge_method,
                                @RequestParam String scope) {
    logger.info("Entered into jwt keyset method");
    RedirectView redirectView = new RedirectView();
    try {
      validateRequestParam(redirect_uri, response_type, response_mode, code_challenge, code_challenge_method, scope);
      String authCode = YekhOneIdentityAuthUtil.getAuthCode();
      mapper.addAuthCode(authCode, code_challenge);

      Map<String, String> model = new HashMap<String, String>();
      model.put("code", authCode);
      redirectView.setUrl(redirect_uri);
      redirectView.setAttributesMap(model);
    } finally {
      //Log the app reporting
    }
    logger.info("Exiting the keyset method");
    return redirectView;
  }

  private void validateRequestParam(String redirect_uri, String response_type, String response_mode, String code_challenge, String code_challenge_method, String scope) {
    if (StringUtils.isBlank(redirect_uri) ||
      StringUtils.isBlank(response_type) ||
      StringUtils.isBlank(response_mode) ||
      StringUtils.isBlank(code_challenge) ||
      StringUtils.isBlank(code_challenge_method) ||
      StringUtils.isBlank(scope)) {
      throw new BadRequestException("invalid_request", "missing mandatory parameters!!");
    }
    if (!"code".equalsIgnoreCase(response_type)) {
      throw new BadRequestException("invalid_request", "invalid response type query param!!");
    }
    if (!"query".equalsIgnoreCase(response_mode)) {
      throw new BadRequestException("invalid_request", "invalid query param!!");
    }
    if (!"S256".equalsIgnoreCase(code_challenge_method)) {
      throw new BadRequestException("invalid_request", "invalid code challenge method query param!!");
    }
    try {
      Scopes.valueOf(scope);
    } catch (Exception exp) {
      throw new BadRequestException("invalid_request", "invalid scope query param!!");
    }
  }

  @PostMapping(value = "/token")
  public ResponseEntity<YekhIdentityToken> token(@RequestBody TokenPayload tokenPayload) {
    logger.debug("starting token method");

    YekhIdentityToken yekhIdentityToken = new YekhIdentityToken();
    try {
      String openIdToken = tokenService.createToken(tokenPayload);
      yekhIdentityToken.setId_token(openIdToken);
    } finally {
      //Logging the app reporting
    }
    logger.debug("exiting token method");
    return new ResponseEntity<>(yekhIdentityToken, HttpStatus.OK);
  }

  @GetMapping(value = "/health")
  public ResponseEntity<String> getHealth() {
    return new ResponseEntity<String>("Application health running", HttpStatus.OK);
  }
}
