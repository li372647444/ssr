package com.ssr.base.model.dynamic;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dynamic_table_manage")
public class DynamicTableManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 主键所属列Id
     */
    @Column(name = "primary_key_column_id")
    private Integer primaryKeyColumnId;

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