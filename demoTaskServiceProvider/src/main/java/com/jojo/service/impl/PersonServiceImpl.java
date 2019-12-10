package com.jojo.service.impl;

import com.jojo.persistent.model.Person;
import com.jojo.service.PersonService;
import com.jojo.service.base.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonServiceImpl extends AbstractService<Person> implements PersonService {

}
