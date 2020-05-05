package com.jojo.task.dynamic.controller;

import com.alibaba.fastjson.JSON;
import com.jojo.persistent.model.SysTask;
import com.jojo.pojo.Response;
import com.jojo.service.SysTaskService;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/dynamic/task")
public class DynamicSysTaskController {

    @Autowired
    private SysTaskService sysTaskService;

    @Autowired
    private Scheduler scheduler;

    private static final Logger logger = LoggerFactory.getLogger(DynamicSysTaskController.class);

    private static final String GROUP_NAME = "testGroup";
    private static final String JOB_NAME_PREFIX = "testTask-";
    private static final String TRIGGER_NAME_SUEFIX = "-trigger";

    @RequestMapping("/list")
    public Response list() {
        Response response = new Response();
        List<SysTask> sysTaskList = sysTaskService.selectAll();
        response.setData(sysTaskList);
        response.setSuccessMessage("");
        return response;
    }

    @RequestMapping("/saveOrUpdate")
    public Response saveOrUpdate(@RequestBody SysTask sysTask) {
        Response response = new Response();

        registerTask(sysTask);

        response.setSuccessMessage("");
        return response;
    }

    @RequestMapping("/delete")
    public Response delete(@RequestBody SysTask sysTask) throws SchedulerException {
        Response response = new Response();

        String jobName = JOB_NAME_PREFIX + sysTask.getId();
        JobKey jobKey = JobKey.jobKey(jobName, GROUP_NAME);

        @SuppressWarnings("unchecked")
        List<Trigger> triggerList = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
        if (CollectionUtils.isEmpty(triggerList)) {
            response.setFailMessage("不存在trigger");
            return response;
        }

        for (Trigger trigger : triggerList) {
            scheduler.pauseTrigger(trigger.getKey());// 暂停
            scheduler.unscheduleJob(trigger.getKey());// 移出
        }
        scheduler.deleteJob(jobKey);
        response.setSuccessMessage("");
        return response;
    }

    @PostConstruct
    private void initAllSysTask() {
        List<SysTask> sysTaskList = sysTaskService.selectAll();
        if (CollectionUtils.isEmpty(sysTaskList)) {
            logger.error("没有需要初始化的task");
            return;
        }
        for (SysTask sysTask : sysTaskList) {
            registerTask(sysTask);
        }
    }

    private void registerTask(SysTask sysTask) {
        try {
            String cron = sysTask.getCron();
            String className = sysTask.getClassName();
            String jobName = JOB_NAME_PREFIX + sysTask.getId();
            String triggerName = jobName + TRIGGER_NAME_SUEFIX;

            // 校验
            boolean cronCheckResult = CronExpression.isValidExpression(cron);
            boolean classCheckResult = ClassUtils.isPresent(className, null);
            if (!(cronCheckResult && classCheckResult)) {// 校验失败退出
                logger.error("此task的配置信息无效：{}", JSON.toJSONString(sysTask));
                return;
            }

            @SuppressWarnings("unchecked")
            Class<Job> classOfJob = (Class<Job>) ClassUtils.forName(className, null);

            // 定义job
            JobKey jobKey = JobKey.jobKey(jobName, GROUP_NAME);
            JobDetail jobDetail = JobBuilder.newJob(classOfJob).withIdentity(jobKey).build();
            // 定义Trigger
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, GROUP_NAME)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(cronScheduleBuilder).build();

            // 注册到scheduler中
            boolean exists = scheduler.checkExists(jobKey);
            if (exists) {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            } else {
                scheduler.scheduleJob(jobDetail, trigger);
            }

            logger.error("{} will run  at: {} , cronExpression is : {}", jobDetail.getKey(), trigger.getNextFireTime(), cron);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
