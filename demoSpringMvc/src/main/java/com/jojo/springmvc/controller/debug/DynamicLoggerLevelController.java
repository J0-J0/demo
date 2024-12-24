package com.jojo.springmvc.controller.debug;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.collect.Lists;
import com.jojo.util.pojo.LoggerVO;
import com.jojo.util.pojo.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debug/logger")
public class DynamicLoggerLevelController {

    private static final String[] LEVEL_STR_ARR = {"debug", "info", "warn", "error"};

    private LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();


    @RequestMapping("/list")
    public Response list() {
        Response response = new Response();

        List<Logger> loggerList = loggerContext.getLoggerList();
        List<LoggerVO> loggerVOList = Lists.newArrayList();
        for (Logger logger : loggerList) {
            Level level = logger.getLevel();
            if (level == null) {
                continue;
            }
            LoggerVO loggerVO = new LoggerVO();
            loggerVO.setName(logger.getName());
            loggerVO.setLevel(logger.getLevel().levelStr);
            loggerVOList.add(loggerVO);
        }

        response.setData(loggerVOList);
        return response;
    }

    @RequestMapping("/updateLevel")
    public Response updateLevel(@RequestParam String loggerName, @RequestParam String loggerLevel) {
        Response response = new Response();

        if (!StringUtils.equalsAnyIgnoreCase(loggerLevel, LEVEL_STR_ARR)) {
            response.setFailMessage("输入logger等级无效");
            return response;
        }

        Logger logger = loggerContext.getLogger(loggerName);
        if (logger == null) {
            response.setFailMessage("此logger不存在");
            return response;
        }

        try {
            logger.setLevel(Level.valueOf(loggerLevel));
            response.setSuccessMessage("变更成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.setFailMessage("变更失败");
        }

        return response;
    }


}
