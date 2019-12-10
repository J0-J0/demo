package com.jojo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jojo.persistent.model.Weather;
import com.jojo.service.WeatherService;
import com.jojo.service.base.AbstractService;

@Service
@Transactional
public class WeatherServiceImpl extends AbstractService<Weather> implements WeatherService {

}
