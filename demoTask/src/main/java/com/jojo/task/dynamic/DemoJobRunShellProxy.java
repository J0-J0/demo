package com.jojo.task.dynamic;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.core.JobRunShell;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DemoJobRunShellProxy extends JobRunShell {

    private static final Logger logger = LoggerFactory.getLogger(DemoJobRunShellProxy.class);

    private TriggerFiredBundle bndle;

    private Long scheduleTaskId;

    public DemoJobRunShellProxy(Scheduler scheduler, TriggerFiredBundle bndleParam) {
        super(scheduler, bndleParam);
        this.bndle = bndleParam;
        scheduleTaskId = bndle.getJobDetail().getJobDataMap().getLong("ScheduleTaskId");
    }

    /**
     * 任务执行前触发
     */
    protected void begin() throws SchedulerException {
//		Long scheduleTaskLogId = SnowFlakerUtil.getSnowflakeId();
//		JobLogFileAppender.contextHolder.set(scheduleTaskLogId);
//		logger.info("begin scheduleTaskId: {} scheduleTaskLogId: {}", scheduleTaskId, scheduleTaskLogId);
//		SysScheduleTaskLog scheduleTaskLog = new SysScheduleTaskLog();
//		scheduleTaskLog.setId(scheduleTaskLogId);
//		scheduleTaskLog.setTaskId(scheduleTaskId);
//		scheduleTaskLog.setStatus(0);
//		scheduleTaskLog.setStartTime(new Date());
//		scheduleTaskLog.setHostname(getHostname());
//		getScheduleTaskLogService().insertSelective(scheduleTaskLog);
        super.begin();
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return null;
    }

    /**
     * 任务完成触发
     */
    protected void complete(boolean successfulExecution) throws SchedulerException {
//		Long scheduleTaskLogId = JobLogFileAppender.contextHolder.get();
//		logger.info("complete scheduleTaskId: {} scheduleTaskLogId: {} successfulExecution:{}", scheduleTaskId,
//				scheduleTaskLogId, successfulExecution);
//		JobLogFileAppender.contextHolder.remove();
//		if (scheduleTaskLogId == null) {
//			return;
//		}
//		SysScheduleTaskLog scheduleTaskLog = new SysScheduleTaskLog();
//		scheduleTaskLog.setId(scheduleTaskLogId);
//		int status = successfulExecution ? 1 : 2;
//		scheduleTaskLog.setEndTime(new Date());
//		scheduleTaskLog.setStatus(status);
//		getScheduleTaskLogService().updateByPrimaryKeySelective(scheduleTaskLog);
        super.complete(successfulExecution);
    }

}
