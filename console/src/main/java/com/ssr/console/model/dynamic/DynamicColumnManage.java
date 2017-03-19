package com.ssr.console.model.dynamic;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dynamic_column_manage")
public class DynamicColumnManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属动态表Id
     */
    @Column(name = "table_id")
    private Integer tableId;

    /**
     * 列名
     */
    @Column(name = "column_name")
    private String columnName;

    /**
     * Mysql 数据库 列  类型
     */
    @Column(name = "type_for_mysql")
    private String typeForMysql;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 小数点
     */
    @Column(name = "decimal_point")
    private Integer decimalPoint;

    /**
     * 枚举值
     */
    @Column(name = "enum_value")
    private String enumValue;

    /**
     * 是否为空
     */
    private Boolean nullable;

    /**
     * 是否为主键
     */
    @Column(name = "is_primary_key")
    private Boolean isPrimaryKey;

    /**
     * 是否自动递增
     */
    @Column(name = "is_autoincrement")
    private Boolean isAutoincrement;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建用户ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 修改用户ID
     */
    @Column(name = "update_user_id")
    private Integer updateUserId;

    
    //------------------不入库字段----------------
    /**
     * 创建用户名
     */
    @Transient
    private String createUserName;
    /**
     * 修改用户名
     */
    @Transient
    private String updateUserName;
  //------------------不入库字段-------end---------
    
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
     * 获取所属动态表Id
     *
     * @return table_id - 所属动态表Id
     */
    public Integer getTableId() {
        return tableId;
    }

    /**
     * 设置所属动态表Id
     *
     * @param tableId 所属动态表Id
     */
    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    /**
     * 获取列名
     *
     * @return column_name - 列名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置列名
     *
     * @param columnName 列名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 获取Mysql 数据库 列  类型
     *
     * @return type_for_mysql - Mysql 数据库 列  类型
     */
    public String getTypeForMysql() {
        return typeForMysql;
    }

    /**
     * 设置Mysql 数据库 列  类型
     *
     * @param typeForMysql Mysql 数据库 列  类型
     */
    public void setTypeForMysql(String typeForMysql) {
        this.typeForMysql = typeForMysql;
    }

    /**
     * 获取长度
     *
     * @return length - 长度
     */
    public Integer getLength() {
        return length;
    }

    /**
     * 设置长度
     *
     * @param length 长度
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 获取小数点
     *
     * @return decimal_point - 小数点
     */
    public Integer getDecimalPoint() {
        return decimalPoint;
    }

    /**
     * 设置小数点
     *
     * @param decimalPoint 小数点
     */
    public void setDecimalPoint(Integer decimalPoint) {
        this.decimalPoint = decimalPoint;
    }

    /**
     * 获取枚举值
     *
     * @return enum_value - 枚举值
     */
    public String getEnumValue() {
        return enumValue;
    }

    /**
     * 设置枚举值
     *
     * @param enumValue 枚举值
     */
    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    /**
     * 获取是否为空
     *
     * @return nullable - 是否为空
     */
    public Boolean getNullable() {
        return nullable;
    }

    /**
     * 设置是否为空
     *
     * @param nullable 是否为空
     */
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * 获取是否为主键
     *
     * @return is_primary_key - 是否为主键
     */
    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * 设置是否为主键
     *
     * @param isPrimaryKey 是否为主键
     */
    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     * 获取是否自动递增
     *
     * @return is_autoincrement - 是否自动递增
     */
    public Boolean getIsAutoincrement() {
        return isAutoincrement;
    }

    /**
     * 设置是否自动递增
     *
     * @param isAutoincrement 是否自动递增
     */
    public void setIsAutoincrement(Boolean isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * 获取创建用户ID
     *
     * @return create_user_id - 创建用户ID
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建用户ID
     *
     * @param createUserId 创建用户ID
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取修改用户ID
     *
     * @return update_user_id - 修改用户ID
     */
    public Integer getUpdateUserId() {
        return updateUserId;
    }

    /**
     * 设置修改用户ID
     *
     * @param updateUserId 修改用户ID
     */
    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
}