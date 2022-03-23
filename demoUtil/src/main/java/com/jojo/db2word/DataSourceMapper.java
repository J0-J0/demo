package com.jojo.db2word;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface DataSourceMapper {
    /**
     * 根据数据库名称获取数据库中表的名称和注释
     *
     * @return
     */
    List<Map<String, Object>> getAllTableNames();

    /**
     * 根据表名称获取表的详细信息
     *
     * @param tableName 数据表名
     * @return
     */
    List<Map<String, Object>> getTableColumnDetail(@Param("tableName") String tableName);
}
