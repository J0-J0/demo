package com.jojo.task.dynamic;

import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.core.JobRunShell;
import org.quartz.core.JobRunShellFactory;
import org.quartz.spi.TriggerFiredBundle;

public class DemoJobRunShellFactoryProxy implements JobRunShellFactory {

	private Scheduler scheduler;

	public JobRunShell createJobRunShell(TriggerFiredBundle arg0) throws SchedulerException {
		return new DemoJobRunShellProxy(this.scheduler, arg0);
	}

	public void initialize(Scheduler arg0) throws SchedulerConfigException {
		this.scheduler = arg0;
	}
}