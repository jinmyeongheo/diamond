package jm.diamond.config.scheduler;

import jm.diamond.batch.BatchHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

  private final SchedulerFactoryBean schedulerFactoryBean;

  @Override
  public void addJob(JobDetail job, LocalDateTime time) {

    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName(job.getJobDataMap().getString(BatchHelper.JOB_NAME_KEY));
    factoryBean.setStartTime(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()));
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    factoryBean.setRepeatInterval(0);
    factoryBean.setRepeatCount(0);
    factoryBean.afterPropertiesSet();

    try {
      schedulerFactoryBean.getScheduler().scheduleJob(job, factoryBean.getObject());

    } catch (SchedulerException e) {
      log.error("error occurred while scheduling with job : {}", job.getKey().getName(), e);
    }
  }

  @Override
  public void addJob(JobDetail job, String cronExpression) {

  }

}
