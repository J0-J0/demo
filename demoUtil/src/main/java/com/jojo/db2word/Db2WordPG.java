package com.jojo.db2word;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import java.util.List;

public class Db2WordPG extends AbstractDb2Word {

    private static String tableSql = "select relname as table_name, " +
            "(select description from pg_description where objoid = oid and objsubid = 0) as table_comment " +
            "from pg_class " +
            "where relkind = 'r' " +
            "group by table_name, table_comment " +
            "order by table_name ";

    private static String tableDetailSql = "select " +
            "a.attname as Field, " +
            "format_type(a.atttypid,a.atttypmod) as Type, " +
            "(case  " +
            "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='p')>0 then 'PRI'  " +
            "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='u')>0 then 'UNI' " +
            "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='f')>0 then 'FRI' " +
            "else '' end) as Key, " +
            "(case when a.attnotnull=true then 'NO' else 'YES' end) as Null, " +
            "col_description(a.attrelid,a.attnum) as Comment " +
            "from pg_attribute a " +
            "where attstattarget=-1 and attrelid = (select oid from pg_class where relname = ? limit 1);";

    @Override
    public List<Entity> queryTableInfo(String schema) throws Exception {
        return Db.use().query(tableSql);
    }

    @Override
    public List<Entity> queryColumnInfo(String tableName) throws Exception {
        return Db.use().query(tableDetailSql, tableName);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(tableSql);

        String outDir = "C:\\Workspace\\test\\dbDetail.doc";

        Db2WordPG mysql = new Db2WordPG();
        mysql.toWord("bookMessage_st", outDir);

    }
}
