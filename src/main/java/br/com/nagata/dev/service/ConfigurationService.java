package br.com.nagata.dev.service;

import br.com.nagata.dev.model.Parameter;
import br.com.nagata.dev.model.enums.PlatformAvailabilityEnum;
import br.com.nagata.dev.repository.ParameterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ConfigurationService {
  private static final String SCHEDULE_PARAMETER = "PARM_SCHEDULE";
  private static final int MINUTES_TO_SUBTRACT = 1;
  private static final int MINUTES_TO_ADD = 1;

  private final ParameterRepository repository;

  @Autowired
  public ConfigurationService(ParameterRepository repository) {
    this.repository = repository;
  }

  public List<String> generateCronTriggers() {
    List<String> cronTriggers = new ArrayList<>();

    Parameter parameter =
        repository
            .findById(SCHEDULE_PARAMETER)
            .orElseThrow(() -> new RuntimeException("Parameter not found"));

    // Runs every Saturday at 23:15
    String cronTrigger1 = "0 15 23 * * SAT";
    cronTriggers.add(cronTrigger1);

    log.info("Opening time: 23:15");
    log.info("Day of week: Saturday");
    log.info("Cron Expression: {}", cronTrigger1);

    // Runs from Monday to Friday at the time defined by the parameter
    String cronTriggerFormat2 = "0 %s %s * * MON-FRI";

    log.info("Opening time: {}", parameter.getOpeningTime());
    log.info("Closing time: {}", parameter.getClosingTime());
    log.info("Period time: {}", parameter.getPeriodTime());

    if (parameter.getOpeningTime().getHour() == parameter.getClosingTime().getHour()) {
      String minute;
      String hour;

      minute =
          parameter.getOpeningTime().getMinute()
              + "-"
              + parameter.getClosingTime().getMinute()
              + "/"
              + parameter.getPeriodTime().getMinute();
      hour = String.valueOf(parameter.getOpeningTime().getHour());

      String cronTrigger2 = String.format(cronTriggerFormat2, minute, hour);
      cronTriggers.add(cronTrigger2);
      log.info("Cron Expression: {}", cronTrigger2);
    } else {
      String minute;
      String hour;

      LocalTime closing =
          LocalTime.of(parameter.getClosingTime().getHour(), LocalTime.MIN.getMinute());
      minute =
          parameter.getOpeningTime().getMinute()
              + (closing.getMinute() == LocalTime.MIN.getMinute() ? "" : "-" + closing.getMinute())
              + "/"
              + parameter.getPeriodTime().getMinute();
      hour =
          parameter.getOpeningTime().getHour()
              + (parameter.getOpeningTime().getHour()
                      == closing.minusMinutes(MINUTES_TO_SUBTRACT).getHour()
                  ? ""
                  : "-" + closing.minusMinutes(MINUTES_TO_SUBTRACT).getHour());

      String cronTrigger2 = String.format(cronTriggerFormat2, minute, hour);
      cronTriggers.add(cronTrigger2);
      log.info("Cron Expression: {}", cronTrigger2);

      minute =
          "0-"
              + (parameter.getClosingTime().getMinute() == LocalTime.MIN.getMinute()
                  ? parameter.getClosingTime().plusMinutes(MINUTES_TO_ADD).getMinute()
                  : parameter.getClosingTime().getMinute())
              + "/"
              + parameter.getPeriodTime().getMinute();
      hour = String.valueOf(parameter.getClosingTime().getHour());

      String cronTrigger3 = String.format(cronTriggerFormat2, minute, hour);
      cronTriggers.add(cronTrigger3);
      log.info("Cron Expression: {}", cronTrigger3);
    }

    return cronTriggers;
  }

  public Boolean validateAsynchronous() {
    LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

    Parameter parameter =
        repository
            .findById(SCHEDULE_PARAMETER)
            .orElseThrow(() -> new RuntimeException("Parameter not found"));

    if (PlatformAvailabilityEnum.OFF == parameter.getPlatformAvailability()) {
      log.warn("Unavailable platform");
      return Boolean.FALSE;
    }

    if ((now.isAfter(parameter.getOpeningTime()) || now.equals(parameter.getOpeningTime()))
        && (now.isBefore(parameter.getClosingTime()) || now.equals(parameter.getClosingTime()))) {
      return Boolean.TRUE;
    }

    log.warn("Processing time closed");
    return Boolean.FALSE;
  }
}
