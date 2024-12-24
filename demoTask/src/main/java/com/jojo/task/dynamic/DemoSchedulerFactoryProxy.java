package com.jojo.task.dynamic;

import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoSchedulerFactoryProxy extends StdSchedulerFactory {

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected Scheduler instantiate(QuartzSchedulerResources rsrcs, QuartzScheduler qs) {
		logger.error("初始化MonitorStdJobRunShellFactory");
		Scheduler scheduler = new StdScheduler(qs);
		try {
			JobRunShellFactory jobFactory = new DemoJobRunShellFactoryProxy();
			jobFactory.initialize(scheduler);
			rsrcs.setJobRunShellFactory(jobFactory);
		} catch (SchedulerConfigException e) {
			logger.error("初始化MonitorStdJobRunShellFactory出错", e);
		}
		return scheduler;
	}
}
