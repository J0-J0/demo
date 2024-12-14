package com.jojo.zzz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);


    static class Person {
        String name;
        String age;
        public Person(String name, String age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    public static void main(String[] args) throws Exception {
        List<Person> list = Lists.newArrayList(new Person("JO", "1"), new Person("JO", "2"),
                 new Person("JO", "2"),new Person("JJ", "2"));
        Map<String, List<Person>> collect = list.stream().collect(Collectors.groupingBy(Person::getName));
        System.out.println(JSON.toJSONString(collect));
    }


}