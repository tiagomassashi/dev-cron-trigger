package br.com.nagata.dev.config;

import br.com.nagata.dev.scheduler.ProcessScheduler;
import br.com.nagata.dev.service.ConfigurationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Log4j2
@Configuration
public class SchedulerConfig {

  private final ConfigurationService service;
  private final ThreadPoolTaskScheduler taskScheduler;
  private final ProcessScheduler processScheduler;
  private final List<ScheduledFuture<?>> scheduledTasks;

  @Autowired
  public SchedulerConfig(
      ConfigurationService service,
      ThreadPoolTaskScheduler taskScheduler,
      ProcessScheduler processScheduler,
      List<ScheduledFuture<?>> scheduledTasks) {
    this.service = service;
    this.taskScheduler = taskScheduler;
    this.processScheduler = processScheduler;
    this.scheduledTasks = new ArrayList<>();
  }

  @EventListener
  public void onReady(ApplicationReadyEvent event) {
    setupTasks();
  }

  public void setupTasks() {
    log.info("Configuring tasks...");
    scheduledTasks.forEach(task -> task.cancel(false));
    scheduledTasks.clear();
    service
        .generateCronTriggers()
        .forEach(cronTrigger -> scheduledTasks.add(createTrigger(cronTrigger)));
  }

  private ScheduledFuture<?> createTrigger(String cronTrigger) {
    return taskScheduler.schedule(processScheduler::start, new CronTrigger(cronTrigger));
  }
}
