package com.ssr.base.model.system;

import javax.persistence.*;

@Table(name = "PRV_ROLE_FUNCTION")
public class PrvRoleFunction {
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * URL权限ID
     */
    @Column(name = "function_id")
    private String functionId;

    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取URL权限ID
     *
     * @return function_id - URL权限ID
     */
    public String getFunctionId() {
        return functionId;
    }

    /**
     * 设置URL权限ID
     *
     * @param functionId URL权限ID
     */
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }
}