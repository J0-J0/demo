package com.jojo.db2word;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.List;

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
