package ${basePackagName}controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ${basePackagName}service.${classSimpleName}Service;

@Controller
@RequestMapping("/api/${uncapitalizedClassSimpleName}")
public class ${classSimpleName}Controller {

    @Autowired
    private ${classSimpleName}Service ${uncapitalizedClassSimpleName}Service;
    
}
