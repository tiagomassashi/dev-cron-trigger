package br.com.nagata.dev.scheduler;

import br.com.nagata.dev.service.ConfigurationService;
import br.com.nagata.dev.service.OperationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProcessScheduler {

  private final OperationService operationService;
  private final ConfigurationService configurationService;

  @Autowired
  public ProcessScheduler(
      OperationService operationService, ConfigurationService configurationService) {
    this.operationService = operationService;
    this.configurationService = configurationService;
  }

  public void start() {
    log.info("Starting validation for asynchronous processing...");
    Boolean generalValidation = configurationService.validateAsynchronous();

    if (!generalValidation) {
      log.info(
          "It will not be possible to start the processes, the general validation returned false");
      return;
    }
    log.info("General validation performed successfully!");
    operationService.execute();
  }
}
