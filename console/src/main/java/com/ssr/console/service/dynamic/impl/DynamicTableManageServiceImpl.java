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
import com.ssr.console.service.dynamic.DynamicTableManageService;

@Service
public class DynamicTableManageServiceImpl extends BaseServiceImpl implements DynamicTableManageService {

	@Autowired
	private DynamicTableManageMapper dynamicTableManageMapper;
	@Autowired
	private DynamicColumnManageMapper dynamicColumnManageMapper;
	
	public DynamicTableManage queryDynamicTableManageById(Object id) {
		return dynamicTableManageMapper.selectByPrimaryKey(id);
	}
	
	public DynamicTableManage saveDynamicTableManage(DynamicTableManage dynamicTableManage) {
		if(dynamicTableManage == null){
            throw new BusinessException("The parameter (dynamicTableManage) must not be null.");
        }
        if(dynamicTableManage.getId() != null){
            throw new BusinessException("The parameter (dynamicTableManage.id) must be null.");
        }
        if(dynamicTableManage.getTableName() == null || "".equals(dynamicTableManage.getTableName())){
            throw new BusinessException("The parameter (dynamicTableManage.tableName) must not be null.");
        }
        DynamicTableManage query = new DynamicTableManage();
        query.setTableName(dynamicTableManage.getTableName());
        List<DynamicTableManage> dynamicTableManages= dynamicTableManageMapper.select(query);
        if(dynamicTableManages!=null && dynamicTableManages.size()>0){
            throw new BusinessException(dynamicTableManage.getTableName()+"表已存在！");
        }
        //插入表 记录
        Date now = new Date();
        dynamicTableManage.setCreateTime(now);
        dynamicTableManageMapper.insert(dynamicTableManage);
        //插入列记录
        DynamicColumnManage idColumn = new DynamicColumnManage();
        idColumn.setTableId(dynamicTableManage.getId());
        idColumn.setColumnName("id");
        idColumn.setTypeForMysql(C_MysqlColumnType.INTEGER);
        idColumn.setLength(10);
        //以下3条必须如下设置
        idColumn.setNullable(false);
        idColumn.setIsPrimaryKey(true);
        idColumn.setIsAutoincrement(true);
        idColumn.setRemark("主键Id 系统新增表时自动添加");
        idColumn.setCreateTime(now);
        int idColumnId = dynamicColumnManageMapper.insert(idColumn);
        idColumn.setId(idColumnId);
        //更新关联
        dynamicTableManage.setPrimaryKeyColumnId(idColumn.getId());
        dynamicTableManageMapper.updateByPrimaryKeySelective(dynamicTableManage);

        //创建表
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE "+dynamicTableManage.getTableName()+" (");
        sql.append(" "+idColumn.getColumnName()+"  "+idColumn.getTypeForMysql()+"("+idColumn.getLength()+") NOT NULL AUTO_INCREMENT COMMENT '"+idColumn.getRemark()+"' , ");
        sql.append(" PRIMARY KEY (`"+idColumn.getColumnName()+"`) ");
        sql.append(") COMMENT='"+dynamicTableManage.getRemark()+"'");
        debug(sql.toString());
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sql", sql.toString());
        dynamicTableManageMapper.executeDDLSql(paraMap);

        return dynamicTableManage;   
	}
	
	public List<DynamicTableManage> queryDynamicTableManageByPage(DynamicTableManage dynamicTableManage, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicTableManageMapper.select(dynamicTableManage);
	}

	public List<DynamicTableManage> queryDynamicTableManageByMap(Map<String, Object> paraMap, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicTableManageMapper.selectDynamicTableManageByMap(paraMap);
	}

	public DynamicTableManage queryByModel(DynamicTableManage dynamicTableManage){
		return dynamicTableManageMapper.selectOne(dynamicTableManage);
	}

	@Override
	public DynamicTableManage updateDynamicTableManage(DynamicTableManage dynamicTableManage) {
		if(dynamicTableManage==null){
            throw new BusinessException("dynamicTableManage 不能为空.");
        }
        if(dynamicTableManage.getId() == null ){
            throw new BusinessException("id不能为空.");
        }
        if(dynamicTableManage.getTableName() == null ){
            throw new BusinessException("表名不能为空.");
        }
        //原表
        DynamicTableManage dynamicTableManage_old = dynamicTableManageMapper.selectByPrimaryKey(dynamicTableManage.getId());
        if(dynamicTableManage_old == null){
            throw new BusinessException("表 记录已不存在！");
        }
        String tableName_old = dynamicTableManage_old.getTableName();
        String remark_old = dynamicTableManage_old.getRemark();
        //判断新表名是否已存在
        DynamicTableManage query = new DynamicTableManage();
        query.setTableName(dynamicTableManage.getTableName());
        DynamicTableManage dynamicTableManage_db = dynamicTableManageMapper.selectOne(query);
        if(dynamicTableManage_db!=null && dynamicTableManage_db.getId() - dynamicTableManage.getId()!=0){
            throw new BusinessException(dynamicTableManage.getTableName()+"表已存！");
        }
        dynamicTableManageMapper.updateByPrimaryKeySelective(dynamicTableManage);
        
        String sql = "ALTER TABLE "+tableName_old+" COMMENT='"+ dynamicTableManage.getRemark() +"'";
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sql", sql.toString());
        dynamicTableManageMapper.executeDDLSql(paraMap);
        try{
            sql = "ALTER TABLE " + tableName_old +" rename "+dynamicTableManage.getTableName();
            paraMap.put("sql", sql.toString());
            dynamicTableManageMapper.executeDDLSql(paraMap);
        }catch (Exception e){//修改表结构，事务无法还原！须抓捕异常，人为还原。
            sql = "ALTER TABLE "+tableName_old+" COMMENT='"+ remark_old +"'";
            paraMap.put("sql", sql.toString());
            dynamicTableManageMapper.executeDDLSql(paraMap);
            throw new BusinessException("更新数据库表名失败!");
        }
        return  dynamicTableManage;
	}

	@Override
	public void deleteDynamicTableManage(Integer id) {
		if(id == null ){
            throw new BusinessException("The parameter (id) must not be null.");
        }
        //删除动态表的列 记录信息
		DynamicColumnManage query = new DynamicColumnManage();
		query.setTableId(id);
        List<DynamicColumnManage> dynamicColumnManages = dynamicColumnManageMapper.select(query);
        if(dynamicColumnManages!=null && dynamicColumnManages.size()>0){
        	for(DynamicColumnManage c:dynamicColumnManages){
        		dynamicColumnManageMapper.deleteByPrimaryKey(c.getId());
        	}
        }

        //删除动态表 记录
        DynamicTableManage dynamicTableManage = dynamicTableManageMapper.selectByPrimaryKey(id);
        if(dynamicTableManage == null){
            throw new BusinessException("删除表 记录已不存在！");
        }
        String tableName = dynamicTableManage.getTableName();
        dynamicTableManageMapper.deleteByPrimaryKey(id);

        //删除 动态表 结构
        String sql = "DROP TABLE IF EXISTS " + tableName;
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("sql", sql.toString());
        dynamicTableManageMapper.executeDDLSql(paraMap);
	}
}
