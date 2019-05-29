package com.jojo.util.http;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 从请求request中获得example信息
 * @author chenyue
 */
public class HttpRequestExampleUtil {

    public final static String EXAMPLE_PARAM_PREX = "_e_";

    /**
     * 从请求中拿到Example的信息 <br>
     * _e_and_name_eq=123&_e_and_status_in=1,2,4,5 ===> WHERE name='123' AND status IN (1,2,4,5) <br>
     * _e_and_name_eq=123&_e_and_status_in=1,2,4,5 ===> WHERE name='123' OR status IN (1,2,4,5) <br>
     * gt gte lt lte like llike rlike notin neq isnull isnotnull
     * @return
     */
    public static Example buildFromRequest(Example example) {
        Map<String, String> request = HttpUtil.getRequestParameters();
        Criteria criteria = example.createCriteria();
        for (Entry<String, String> e : request.entrySet()) {
            List<String> items = Splitter.on("_").omitEmptyStrings().splitToList(e.getKey());
            if (!StringUtils.startsWith(e.getKey(), EXAMPLE_PARAM_PREX)) {
                continue;
            }
            if (items.size() < 4) {
                continue;
            }
            String operation = items.get(1);
            String property = items.get(2);
            String exampleOperation = items.get(3);
            String value = e.getValue();
            if (StringUtils.equalsIgnoreCase(operation, "or")) {
                criteria = example.or();
            }
            if (StringUtils.equalsIgnoreCase(exampleOperation, "eq")) {
                criteria.andEqualTo(property, value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "neq")) {
                criteria.andNotEqualTo(property, value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "in")) {
                criteria.andIn(property, Splitter.on(",").splitToList(value));
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "notin")) {
                criteria.andNotIn(property, Splitter.on(",").splitToList(value));
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "gt")) {
                criteria.andGreaterThan(property, value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "gte")) {
                criteria.andGreaterThanOrEqualTo(property, value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "lt")) {
                criteria.andLessThan(property, value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "lte")) {
                criteria.andLessThanOrEqualTo(property, value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "like")) {
                criteria.andLike(property, "%" + value + "%");
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "llike")) {
                criteria.andLike(property, value + "%");
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "rlike")) {
                criteria.andLike(property, "%" + value);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "notlike")) {
                criteria.andNotLike(property, "%" + value + "%");
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "isnull")) {
                criteria.andIsNull(property);
            } else if (StringUtils.equalsIgnoreCase(exampleOperation, "isnotnull")) {
                criteria.andIsNotNull(property);
            }
        }
        return example;
    }

}
