package com.jojo.util.db2word;

import java.util.List;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

public class Db2WordMysql extends AbstractDb2Word {

    private static String tableSql = "select table_name, table_comment from information_schema.tables where table_schema = ?";
    private static String tableDetailSql = "SHOW FULL FIELDS FROM ";

    @Override
    public List<Entity> queryTableInfo(String schema) throws Exception {
        return Db.use().query(tableSql, schema);
    }

    @Override
    public List<Entity> queryColumnInfo(String tableName) throws Exception {
        return Db.use().query(tableDetailSql + tableName);
    }

    public static void main(String[] args) throws Exception {
        String outDir = "C:\\Workspace\\test\\dbDetail.doc";

        Db2WordMysql mysql = new Db2WordMysql();
        mysql.toWord("bookMessage_st", outDir);
    }
}
