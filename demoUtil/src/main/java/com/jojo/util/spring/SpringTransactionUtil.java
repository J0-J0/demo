package com.jojo.util.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class SpringTransactionUtil {

    private static Logger logger = LoggerFactory.getLogger(SpringTransactionUtil.class);

    /**
     * 开启新事务
     *
     * @return
     */
    public static TransactionStatus getTransaction() {
        // 默认bean的名字
        return getTransaction("transactionManager");
    }

    /**
     * 回滚
     */
    public static void rollback(TransactionStatus status) {
        rollback("transactionManager", status);
    }

    /**
     * 提交
     */
    public static void commit(TransactionStatus status) {
        commit("transactionManager", status);
    }

    /**
     * 获取指定的事务管理bean
     *
     * @param transactionManagerBeanName
     * @return
     */
    public static TransactionStatus getTransaction(String transactionManagerBeanName) {
        DataSourceTransactionManager transactionManager = SpringContextUtil.getBean(transactionManagerBeanName);
        if (transactionManager == null) {
            logger.error("此name：{}没有获取到bean", transactionManagerBeanName);
            return null;
        }

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 传播方式
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 隔离级别
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // 获得事务状态
        TransactionStatus status = transactionManager.getTransaction(definition);

        return status;
    }

    public static void rollback(String transactionManagerBeanName, TransactionStatus status) {
        DataSourceTransactionManager transactionManager = SpringContextUtil.getBean(transactionManagerBeanName);
        if (transactionManager == null) {
            logger.error("此name：{}没有获取到bean", transactionManagerBeanName);
            return;
        }
        transactionManager.rollback(status);
    }

    public static void commit(String transactionManagerBeanName, TransactionStatus status) {
        DataSourceTransactionManager transactionManager = SpringContextUtil.getBean(transactionManagerBeanName);
        if (transactionManager == null) {
            logger.error("此name：{}没有获取到bean", transactionManagerBeanName);
            return;
        }
        transactionManager.commit(status);
    }
}
