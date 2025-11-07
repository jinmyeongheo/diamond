package jm.diamond.batch;


import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Quartz Schedule 에 등록된 Job 을 Spring Batch Job 으로 실행시키기 위한 Executor class.
 */
@Slf4j
@DisallowConcurrentExecution
public class BatchJobExecutor implements Job {
    
    @Autowired
    private JobLocator jobLocator;
    
    @Autowired
    private JobLauncher jobLauncher;

    /**
     * Quartz Job 으로 들어온 Parameter 를 Spring Batch Parameter 로 변환하여 Spring Batch Job 실행
     * 
     * @param context quartz execution context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            //beforeExecute(context);
            String jobName = BatchHelper.getJobName(context.getMergedJobDataMap());
            log.info("[{}] started.", jobName);
            JobParameters jobParameters = BatchHelper.getJobParameters(context);
            if (jobParameters == null) {
                jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
            }
            log.info("[{}] completed.", jobName);
            //afterExecute(context);
            //scheduleNextJob(context);
        } catch (NoSuchJobException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | SchedulerException e) {
            log.error("job execution exception! - {}", e);
            throw new JobExecutionException();
        }
    }

    //job chainning 관련 프로세스이나 시간문제로 추후 구현으로 보류
    /*
    private void beforeExecute(JobExecutionContext context) {
        log.info("%%% Before executing job");
    }

    protected abstract void doExecute(JobExecutionContext context);

    private void afterExecute(JobExecutionContext context) {
        log.info("%%% After executing job");
        Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
        List<JobDetail> jobDetailQueue = (List<JobDetail>) object;

        if (jobDetailQueue.size() > 0) {
            jobDetailQueue.remove(0);
        }
    }

    private void scheduleNextJob(JobExecutionContext context) {
        log.info("$$$ Schedule Next Job");
        Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
        List<JobDetail> jobDetailQueue = (List<JobDetail>) object;

        if (jobDetailQueue.size() > 0) {
            JobDetail nextJobDetail = jobDetailQueue.get(0);
            nextJobDetail.getJobDataMap().put("JobDetailQueue", jobDetailQueue);
            Trigger nowTrigger = newTrigger().startNow().build();

            try {
                // 아래의 팩토리 메서드는 이름이 같으면 여러번 호출해도 항상 동일한 스케줄러를 반환한다.
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                scheduler.scheduleJob(nextJobDetail, nowTrigger);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }
    */
}
