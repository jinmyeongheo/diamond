package jm.diamond.config.scheduler;

import org.quartz.JobDetail;

import java.time.LocalDateTime;

public interface SchedulerService {
  
  void addJob(JobDetail job, LocalDateTime time);

  void addJob(JobDetail job, String cronExpression);
}
