package com.jojo.service.base;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class AbstractService<T> {

    @Autowired(required = false)
    private Mapper<T> mapper;

    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractService() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        this.persistentClass = (Class<T>) actualTypeArguments[0];
    }

    protected Example buildExample() {
        return new Example(persistentClass);
    }

    public int insert(T entity) {
        return mapper.insert(entity);
    }

    public int insertSelective(T entity) {
        return mapper.insertSelective(entity);
    }

    public int deleteByPrimaryKey(Object entity) {
        return mapper.deleteByPrimaryKey(entity);
    }

    public int deleteByExample(Example example) {
        return mapper.deleteByExample(example);
    }

    public int deleteAll() {
        return mapper.deleteByExample(null);
    }

    public List<T> selectAll() {
        return mapper.selectAll();
    }

    public T selectByPrimaryKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    public T selectOneByExample(Example example) {
        PageHelper.offsetPage(0, 1, false);
        List<T> rows = mapper.selectByExample(example);
        T one = null;
        if (CollectionUtils.isNotEmpty(rows)) {
            one = rows.get(0);
        } else {
            throw new RuntimeException("too many result：" + JSON.toJSONString(rows));
        }
        return one;
    }

    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    public int count(T record) {
        return mapper.selectCount(record);
    }

    public int countByExample(Example example) {
        return mapper.selectCountByExample(example);
    }

    public int countAll() {
        return mapper.selectCountByExample(null);
    }

    public int updateByExampleSelective(T record, Example example) {
        return mapper.updateByExampleSelective(record, example);
    }

    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

}