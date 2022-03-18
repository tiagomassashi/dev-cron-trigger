package br.com.nagata.dev.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Log4j2
@Service
public class OperationService {

  public void execute() {
    log.info("Executing process...");
    Instant instant = Instant.now();

    // TODO implement method

    log.info("Complete process, runtime: {}", instant.until(Instant.now(), ChronoUnit.MILLIS));
  }
}
