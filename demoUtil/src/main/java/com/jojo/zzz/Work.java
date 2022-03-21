
package com.jojo.zzz;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    public static void main(String[] args) throws Exception {
        List<Entity> entityList = Db.use().query("select table_name AS table_name, table_comment AS table_comment from information_schema.tables where table_schema = ?",
                "bookmessage_st");

        for (Entity entity : entityList) {
            System.out.println("table_name: " + entity.getStr("table_name"));
            System.out.println("table_comment: " + entity.getStr("table_comment"));
        }
    }


}