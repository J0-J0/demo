package com.jojo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jojo.persistent.model.Scores;
import com.jojo.service.ScoresService;
import com.jojo.service.base.AbstractService;

@Service
@Transactional
public class ScoresServiceImpl extends AbstractService<Scores> implements ScoresService {

}
