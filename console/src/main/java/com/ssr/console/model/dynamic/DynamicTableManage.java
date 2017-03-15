package com.ssr.console.model.dynamic;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dynamic_table_manage")
public class DynamicTableManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 主键所属列Id
     */
    @Column(name = "primary_key_column_id")
    private Integer primaryKeyColumnId;

    /**
     * 描述
     */
    private String remark;

    /**
     * 表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取主键所属列Id
     *
     * @return primary_key_column_id - 主键所属列Id
     */
    public Integer getPrimaryKeyColumnId() {
        return primaryKeyColumnId;
    }

    /**
     * 设置主键所属列Id
     *
     * @param primaryKeyColumnId 主键所属列Id
     */
    public void setPrimaryKeyColumnId(Integer primaryKeyColumnId) {
        this.primaryKeyColumnId = primaryKeyColumnId;
    }

    /**
     * 获取描述
     *
     * @return remark - 描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置描述
     *
     * @param remark 描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取表名
     *
     * @return table_name - 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     *
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}