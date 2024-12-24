package com.jojo.task.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTask implements Job {

    private static final Logger logger = LoggerFactory.getLogger(TestTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.error("===测试quartz开始执行===");
        logger.error("===测试quartz执行结束===");
    }

    public static void main(String[] args) throws SchedulerException, InterruptedException {

        String jobName = "testTask";
        String jobTriggerName = jobName + "Trigger";
        String jobGroupName = "testGroup";

        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        JobDetail job = JobBuilder.newJob(TestTask.class).withIdentity(jobKey).build();

        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobTriggerName, jobGroupName)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ? "))
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);

        System.out.println(job.getKey() + " will run  at: " + trigger.getNextFireTime());

//        scheduler.start();

        System.out.println(scheduler.checkExists(jobKey));
        System.out.println(scheduler.getTriggersOfJob(jobKey));

//        Thread.sleep(10 * 1000);
//
//        scheduler.shutdown();
    }
}
