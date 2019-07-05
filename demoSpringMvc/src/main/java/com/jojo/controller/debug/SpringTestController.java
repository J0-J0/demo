package com.jojo.controller.debug;

import com.jojo.pojo.Response;
import com.jojo.util.spring.SpringContextUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class SpringTestController {

    @RequestMapping(value = "/getBean", method = RequestMethod.GET)
    public Response getBean(@RequestParam("name") String name) {
        Response response = new Response();
        response.setCode(1);
        response.setData(SpringContextUtil.getType(ThreadPoolTaskExecutor.class));
        return response;
    }
}
