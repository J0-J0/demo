package com.jojo.task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TestTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("===测试quartz开始执行===");
        System.out.println("===测试quartz执行结束===");
    }

    public static void main(String[] args) throws SchedulerException, InterruptedException {

        String jobName = "testTask";
        String jobTriggerName = jobName + "Trigger";
        String jobGroupName = "testGroup";

        JobDetail job = JobBuilder.newJob(TestTask.class)
                .withIdentity(jobName, jobGroupName)
                .build();

        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobTriggerName, jobGroupName)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ? "))
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);


        scheduler.start();

        Thread.sleep(10 * 1000);

        scheduler.shutdown();
    }
}
