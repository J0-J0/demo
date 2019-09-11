package com.jojo.persistent.service.impl;

import com.google.common.collect.Lists;
import com.jojo.persistent.mapper.SCMapper;
import com.jojo.persistent.model.SC;
import com.jojo.persistent.model.SysTask;
import com.jojo.persistent.service.SysTaskService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Random;

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

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext-persistent.xml");
        SCMapper scMapper = ctx.getBean(SCMapper.class);

        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) ctx.getBean("transactionManager");
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        TransactionStatus status = transactionManager.getTransaction(definition);

        SC sc = new SC();
        Random random = new Random();
        sc.setCno(random.nextInt());
        List<SC> scList = Lists.newArrayList();
        scList.add(sc);
        scList.add(sc);
        scList.add(sc);
        scList.add(sc);
        scList.add(sc);

        scMapper.insertList(scList);

        transactionManager.commit(status);
    }

}
