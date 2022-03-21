package com.yekh.identity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
public class YekhOneIdentityBoot {
  private static final Logger logger = LoggerFactory.getLogger(YekhOneIdentityBoot.class);

  public static void main(final String[] args) {
    /*String port = System.getenv("PORT") != null ? System.getenv("PORT") : "8081";
    if (port == null) {
      logger.warn("$PORT environment variable not set");
    }*/
    SpringApplication.run(YekhOneIdentityBoot.class, args);
  }

}
