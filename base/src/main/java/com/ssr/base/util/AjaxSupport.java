package com.ssr.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ajax请求返回结构封装
 *
 * @author PengLian
 */
public class AjaxSupport<T> {
    /**
     * 业务处理结果
     */
    private Boolean success = true;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 错误提示信息
     */
    private String errorMessage;
    /**
     * 数据实体
     */
    private T model;
    /**
     * 数据实体列表
     */
    private List<T> entities = new ArrayList<T>();
    /**
     * 额外参数键值对
     */
    private Map<String, Object> params = new HashMap<String, Object>();
    /**
     * 额外错误参数键值对
     */
    private Map<String, String> errors = new HashMap<String, String>();
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getModel() {
        return model;
    }
    public void setModel(T model) {
        this.model = model;
    }
    public List<T> getEntities() {
        return entities;
    }
    public void setEntities(List<T> entities) {
        this.entities = entities;
    }
    public Map<String, Object> getParams() {
        return params;
    }
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
