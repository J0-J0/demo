package com.jojo.boot.controller;

import com.jojo.pojo.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/helloSpringBoot")
    public Response helloSpringBoot() {
        return Response.succeed("helloSpringBoot");
    }
}
