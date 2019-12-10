package com.jojo.service.impl;

import com.jojo.persistent.model.Employee;
import com.jojo.service.EmployeeService;
import com.jojo.service.base.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl extends AbstractService<Employee> implements EmployeeService {

}
