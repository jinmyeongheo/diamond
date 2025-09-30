package jm.diamond.batch;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzTest {

    public static void main(String[] args) throws SchedulerException {
        // Step 1: Define the Job

        JobDataMap jobDataMap = new JobDataMap(); // job Data Map
        jobDataMap.put("executeCount", 1); // put Data

        JobDetail jobDetail = newJob().withIdentity("job1", "group1")
                .withDescription("job1-description")
                .usingJobData(jobDataMap)
                .build();

        // Step 2: Define the Trigger
        Trigger trigger = newTrigger()
                .withIdentity("timeTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        // Step 3: Schedule the job
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
