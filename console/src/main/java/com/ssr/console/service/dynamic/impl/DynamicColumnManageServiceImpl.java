package com.ssr.console.service.dynamic.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.ssr.base.exception.BusinessException;
import com.ssr.base.service.impl.BaseServiceImpl;
import com.ssr.base.util.constant.C_MysqlColumnType;
import com.ssr.console.mapper.dynamic.DynamicColumnManageMapper;
import com.ssr.console.mapper.dynamic.DynamicTableManageMapper;
import com.ssr.console.model.dynamic.DynamicColumnManage;
import com.ssr.console.model.dynamic.DynamicTableManage;
import com.ssr.console.service.dynamic.DynamicColumnManageService;

@Service
public class DynamicColumnManageServiceImpl extends BaseServiceImpl implements DynamicColumnManageService {

	@Autowired
	private DynamicTableManageMapper dynamicTableManageMapper;
	@Autowired
	private DynamicColumnManageMapper dynamicColumnManageMapper;
	@Override
	public DynamicColumnManage queryDynamicColumnManageById(Object id) {
		return dynamicColumnManageMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<DynamicColumnManage> queryDynamicColumnManageByPage(DynamicColumnManage dynamicColumnManage, int page,int pageSize) {
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicColumnManageMapper.select(dynamicColumnManage);
	}
	@Override
	public DynamicColumnManage queryByModel(DynamicColumnManage dynamicColumnManage) {
		return dynamicColumnManageMapper.selectOne(dynamicColumnManage);
	}
	@Override
	public List<DynamicColumnManage> queryDynamicColumnManageByMap(Map<String, Object> paraMap, int page,int pageSize) {
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicColumnManageMapper.selectDynamicColumnManageByMap(paraMap);
	}
	@Override
	public DynamicColumnManage updateDynamicColumnManage(DynamicColumnManage dynamicColumnManage) {
		if(dynamicColumnManage==null){
            throw new BusinessException("dynamicColumnManage 不能为空.");
        }
        if(dynamicColumnManage.getId() == null ){
            throw new BusinessException("id不能为空.");
        }
        if(dynamicColumnManage.getColumnName() == null ){
            throw new BusinessException("列名不能为空.");
        }
        if(dynamicColumnManage.getTableId() == null ){
            throw new BusinessException("所属动态表Id不能为空.");
        }
        if(dynamicColumnManage.getTypeForMysql() == null ){
            throw new BusinessException("Mysql 数据库 列  类型不能为空！");
        }
        //原列
        DynamicColumnManage dynamicColumnManage_old = dynamicColumnManageMapper.selectByPrimaryKey(dynamicColumnManage.getId());
        if(dynamicColumnManage_old == null){
            throw new BusinessException("列 记录已不存在！");
        }
        DynamicTableManage dynamicTableManage = dynamicTableManageMapper.selectByPrimaryKey(dynamicColumnManage.getTableId());
        if(dynamicTableManage == null){
            throw new BusinessException("列的表记录已不存在！");
        }
        String tableName = dynamicTableManage.getTableName();
        String columnName_old = dynamicColumnManage_old.getColumnName();
        //判断新列名是否已存在
        DynamicColumnManage query = new DynamicColumnManage();
        query.setTableId(dynamicColumnManage.getTableId());
        query.setColumnName(dynamicColumnManage.getColumnName());
        List<DynamicColumnManage> dynamicColumnManages = dynamicColumnManageMapper.select(query);
        if(dynamicColumnManages!=null && dynamicColumnManages.size()>0
                && (dynamicColumnManages.get(0).getId() - dynamicColumnManage.getId()!=0)){
            throw new BusinessException(dynamicColumnManage.getColumnName()+"列已存！");
        }
        if(dynamicColumnManage.getIsAutoincrement()==null){
			dynamicColumnManage.setIsAutoincrement(false);
		}
		if(dynamicColumnManage.getIsPrimaryKey()==null){
			dynamicColumnManage.setIsPrimaryKey(false);
		}
        dynamicColumnManageMapper.updateByPrimaryKey(dynamicColumnManage);

        String sql = "ALTER TABLE "+ tableName +" CHANGE COLUMN "+ columnName_old +" "+ dynamicColumnManage.getColumnName() +" ";
        //类型 与 长度
        if(C_MysqlColumnType.INTEGER.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql()+"("+ dynamicColumnManage.getLength() +(dynamicColumnManage.getDecimalPoint()==null?"":","+dynamicColumnManage.getDecimalPoint())+")";
        } else if(C_MysqlColumnType.VARCHAR.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql()+"("+ dynamicColumnManage.getLength() +")";
        } else if(C_MysqlColumnType.DOUBLE.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql()+"("+ dynamicColumnManage.getLength() +(dynamicColumnManage.getDecimalPoint()==null?"0":","+dynamicColumnManage.getDecimalPoint())+")";
        } else if(C_MysqlColumnType.DATETIME.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql();
        } else {
            throw new BusinessException("未知列类型!");
        }
        //是否为空
        if(dynamicColumnManage.getNullable()){
            sql += " NULL ";
        } else {
            sql += " NOT NULL ";
        }
        sql += " COMMENT '"+ dynamicColumnManage.getRemark() +"'";
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sql", sql.toString());
        dynamicTableManageMapper.executeDDLSql(paraMap);

        return  dynamicColumnManage;
	}
	@Override
	public DynamicColumnManage saveDynamicColumnManage(DynamicColumnManage dynamicColumnManage) {
		if(dynamicColumnManage == null){
            throw new BusinessException("The parameter (dynamicColumnManage) must not be null.");
        }
        if(dynamicColumnManage.getId() != null){
            throw new BusinessException("The parameter (dynamicColumnManage.id) must be null.");
        }
        if(dynamicColumnManage.getColumnName() == null || "".equals(dynamicColumnManage.getColumnName())){
            throw new BusinessException("The parameter (dynamicColumnManage.columnName) must not be null.");
        }
        if(dynamicColumnManage.getTableId() == null || "".equals(dynamicColumnManage.getTableId())){
            throw new BusinessException("The parameter (dynamicColumnManage.tableId) must not be null.");
        }
        if(dynamicColumnManage.getTypeForMysql() == null || "".equals(dynamicColumnManage.getTypeForMysql())){
            throw new BusinessException("The parameter (dynamicColumnManage.typeForMysql) must not be null.");
        }
        if(dynamicColumnManage.getLength() == null || "".equals(dynamicColumnManage.getLength())){
            throw new BusinessException("The parameter (dynamicColumnManage.length) must not be null.");
        }
        DynamicTableManage dynamicTableManage = dynamicTableManageMapper.selectByPrimaryKey(dynamicColumnManage.getTableId());
        if(dynamicTableManage == null){
            throw new BusinessException("列的表记录已不存在！");
        }
        DynamicColumnManage query = new DynamicColumnManage();
        query.setTableId(dynamicColumnManage.getTableId());
        query.setColumnName(dynamicColumnManage.getColumnName());
        List<DynamicColumnManage> dynamicColumnManages = dynamicColumnManageMapper.select(query);
        if(dynamicColumnManages!=null && dynamicColumnManages.size()>0){
            throw new BusinessException(dynamicTableManage.getTableName()+"列已存在！");
        }
        //插入列记录
        dynamicColumnManage.setCreateTime(new Date());
        if(dynamicColumnManage.getIsAutoincrement()==null){
			dynamicColumnManage.setIsAutoincrement(false);
		}
		if(dynamicColumnManage.getIsPrimaryKey()==null){
			dynamicColumnManage.setIsPrimaryKey(false);
		}
        dynamicColumnManageMapper.insert(dynamicColumnManage);

        //创建列
        String tableName = dynamicTableManage.getTableName();
        String sql = "ALTER TABLE "+ tableName +" ADD COLUMN "+ dynamicColumnManage.getColumnName() +" ";
        //类型 与 长度
        if(C_MysqlColumnType.INTEGER.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql()+"("+ dynamicColumnManage.getLength() +(dynamicColumnManage.getDecimalPoint()==null?"":","+dynamicColumnManage.getDecimalPoint())+")";
        } else if(C_MysqlColumnType.VARCHAR.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql()+"("+ dynamicColumnManage.getLength() +")";
        } else if(C_MysqlColumnType.DOUBLE.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql()+"("+ dynamicColumnManage.getLength() +(dynamicColumnManage.getDecimalPoint()==null?"0":","+dynamicColumnManage.getDecimalPoint())+")";
        } else if(C_MysqlColumnType.DATETIME.equals(dynamicColumnManage.getTypeForMysql())){
            sql += dynamicColumnManage.getTypeForMysql();
        } else {
            throw new BusinessException("未知列类型！");
        }
        //是否为空
        if(dynamicColumnManage.getNullable()){
            sql += " NULL ";
        } else {
            sql += " NOT NULL ";
        }
        sql += " COMMENT '"+ dynamicColumnManage.getRemark() +"'";
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sql", sql.toString());
        dynamicTableManageMapper.executeDDLSql(paraMap);

        return dynamicColumnManage;
	}
	@Override
	public void deleteDynamicColumnManage(Integer id) {
	 	if(id == null ){
            throw new BusinessException("The parameter (id) must not be null.");
        }
        //删除动态列 记录
        DynamicColumnManage dynamicColumnManage = dynamicColumnManageMapper.selectByPrimaryKey(id);
        if(dynamicColumnManage == null){
            throw new BusinessException("删除列 记录已不存在！");
        }
        DynamicTableManage dynamicTableManage = dynamicTableManageMapper.selectByPrimaryKey(dynamicColumnManage.getTableId());
        if(dynamicTableManage == null){
            throw new BusinessException("删除列的表记录已不存在！");
        }
        String tableName = dynamicTableManage.getTableName();
        String columnName = dynamicColumnManage.getColumnName();
        dynamicColumnManageMapper.delete(dynamicColumnManage);

        //删除 动态列 结构
        String sql = "ALTER TABLE "+ tableName +" DROP COLUMN "+columnName;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sql", sql.toString());
        dynamicTableManageMapper.executeDDLSql(paraMap);
	}
	
}
