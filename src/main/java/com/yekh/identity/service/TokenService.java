package com.yekh.identity.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.yekh.identity.bean.AuthCodeMapper;
import com.yekh.identity.bean.TokenPayload;
import com.yekh.identity.util.YekhOIDCKeyPair;
import com.yekh.identity.exception.BadRequestException;
import com.yekh.identity.exception.UpstreamSystemFailedException;
import com.yekh.identity.util.AuthorizedGrantTypes;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Base64;
import java.util.List;

@Service
public class TokenService {
  private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  @Autowired
  private AuthCodeMapper mapper;

  @Autowired
  private YekhOIDCKeyPair keySet;

  @Autowired
  private Environment env;

  private String issueJWT(JWK jwk) throws JOSEException, ParseException {
    JSONObject payload = new JSONObject();
    payload.put("sub", "kcvluv@gmail.com");
    payload.put("iss", env.getProperty("openid.issuer"));
    payload.put("iat", System.currentTimeMillis());
    payload.put("exp", System.currentTimeMillis() + (10 * 60 * 1000));

    JWSSigner signer = new ECDSASigner(ECKey.parse(jwk.toJSONString()).toECPrivateKey());
    JWSObject jwsObject = new JWSObject(
      new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(jwk.getKeyID()).build(),
      new Payload(payload));
    jwsObject.sign(signer);

    String jwtAsString = jwsObject.serialize();
    return jwtAsString;
  }

  public String createToken(TokenPayload tokenPayload) {
    validatePayload(tokenPayload);
    if (validateRequest(tokenPayload.getCode(), tokenPayload.getCode_verifier())) {
      JWKSet jwkSet = keySet.getPrivateKeySet();
      List<JWK> jwkList = jwkSet.getKeys();
      JWK privateJWK = jwkList.get(0);
      try {
        String token = this.issueJWT(privateJWK);
        if (StringUtils.isNotBlank(token)) {
          mapper.invalidateAuthCode(tokenPayload.getCode());
        }
        return token;
      } catch (JOSEException e) {
        logger.error("failed to create the jwt token");
        throw new UpstreamSystemFailedException("system_error", "failed to create jwt string");
      } catch (ParseException e) {
        logger.error("failed to parse the key while creating the jwt token");
        throw new UpstreamSystemFailedException("system_error", "failed to create jwt string");
      }
    }
    return null;
  }

  private void validatePayload(TokenPayload tokenPayload) {
    if (StringUtils.isBlank(tokenPayload.getCode()) || StringUtils.isBlank(tokenPayload.getCode_verifier())) {
      throw new BadRequestException("invalid_request", "code and code_verifier are mandatory!!");
    }
    try {
      AuthorizedGrantTypes.valueOf(tokenPayload.getGrant_type());
    } catch (Exception exp) {
      throw new BadRequestException("invalid_request", "invalid authorization grant type!!");
    }
    if (mapper.getCodeChallenge(tokenPayload.getCode()) == null) {
      throw new BadRequestException("invalid_request", "invalid authorization code");
    }
  }

  private boolean validateRequest(String code, String code_verifier) {

    String sha256hexCodeVerifier = DigestUtils.sha256Hex(code_verifier);
    String encodedURLStr = Base64.getUrlEncoder()
      .encodeToString(sha256hexCodeVerifier.getBytes());

    if (encodedURLStr.equalsIgnoreCase(mapper.getCodeChallenge(code))) {
      throw new BadRequestException("invalid_request", "invalid authorization code");
    }
    return true;
  }
}
