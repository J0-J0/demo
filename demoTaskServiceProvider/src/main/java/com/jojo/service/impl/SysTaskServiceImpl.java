package com.jojo.service.impl;

import com.google.common.collect.Lists;
import com.jojo.persistent.model.SysTask;
import com.jojo.service.SysTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTaskServiceImpl implements SysTaskService {

    public List<SysTask> selectAll() {
        SysTask sysTask = new SysTask();
        sysTask.setId((long) 123456);
        sysTask.setClassName("com.jojo.task.TestTask");
        sysTask.setName("testTask");
        sysTask.setCron("0/1 * * * * ? ");
        sysTask.setStatus(1);

        return Lists.newArrayList(sysTask);
    }
}
