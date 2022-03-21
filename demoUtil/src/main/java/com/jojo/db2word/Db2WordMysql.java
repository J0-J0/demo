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

public class Db2WordMysql {

    public static String tableSql = "select table_name, table_comment from information_schema.tables where table_schema = ?";
    public static String tableDetailSql = "SHOW FULL FIELDS FROM ";

    public static void toWord(String schema, String outDir) throws Exception {

        List<Entity> tableEntityList = Db.use().query(tableSql, schema);

        // 创建word文档,并设置纸张的大小
        Document document = new Document(PageSize.A4);
        // 创建word文档
        RtfWriter2.getInstance(document, new FileOutputStream(outDir));
        document.open();// 设置文档标题
        Paragraph p = new Paragraph("数据库表设计文档", new Font(Font.NORMAL, 24, Font.BOLD, new Color(0, 0, 0)));
        p.setAlignment(1);
        document.add(p);

        /* * 创建表格 通过查询出来的表遍历 */
        for (int i = 0; i < tableEntityList.size(); i++) {
            // 表名
            String table_name = tableEntityList.get(i).getStr("table_name");
            // 表说明
            String table_comment = StringUtils.isBlank(tableEntityList.get(i).getStr("table_comment")) ? "" : tableEntityList.get(i).getStr("table_comment");

            //获取某张表的所有字段说明
            List<Entity> columnEntityList = Db.use().query(tableDetailSql + table_name);
            //构建表说明
            String all = "" + (i + 1) + ". 表名：" + table_name + " " + table_comment + "";
            //创建有6列的表格
            Table table = new Table(6);
            document.add(new Paragraph(""));
            table.setBorderWidth(1);
            table.setPadding(0);
            table.setSpacing(0);

            /*
             * 添加表头的元素
             */
            Cell cell = new Cell("序号");// 单元格
            cell.setHeader(true);
            table.addCell(cell);

            cell = new Cell("名称");// 单元格
            table.addCell(cell);

            cell = new Cell("类型");// 单元格
            table.addCell(cell);

            cell = new Cell("允许为空");// 单元格
            table.addCell(cell);

            cell = new Cell("索引");// 单元格
            table.addCell(cell);

            cell = new Cell("说明");// 单元格
            table.addCell(cell);

            table.endHeaders();// 表头结束

            // 表格的主体
            for (int k = 0; k < columnEntityList.size(); k++) {
                //获取某表每个字段的详细说明
                String Field = columnEntityList.get(k).getStr("Field");
                String Type = columnEntityList.get(k).getStr("Type");
                String Null = columnEntityList.get(k).getStr("Null");
                String Key = columnEntityList.get(k).getStr("Key");
                String Comment = columnEntityList.get(k).getStr("Comment");
                table.addCell((k + 1) + "");
                table.addCell(Field);
                table.addCell(Type);
                table.addCell(Null);
                table.addCell(Key);
                table.addCell(Comment);
            }
            Paragraph paragraph = new Paragraph(all);
            //写入表说明
            document.add(paragraph);
            //生成表格
            document.add(table);
        }
        document.close();
    }

    public static void main(String[] args) throws Exception {
        String outDir = "C:\\Workspace\\test\\dbDetail.doc";
        toWord("bookMessage_st", outDir);
    }
}
