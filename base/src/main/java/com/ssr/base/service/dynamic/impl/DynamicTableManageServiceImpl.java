package com.ssr.base.service.dynamic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.ssr.base.exception.BusinessException;
import com.ssr.base.mapper.dynamic.DynamicColumnManageMapper;
import com.ssr.base.mapper.dynamic.DynamicTableManageMapper;
import com.ssr.base.model.dynamic.DynamicColumnManage;
import com.ssr.base.model.dynamic.DynamicTableManage;
import com.ssr.base.service.dynamic.DynamicTableManageService;
import com.ssr.base.service.impl.BaseServiceImpl;
import com.ssr.base.util.constant.C_MysqlColumnType;
import com.ssr.base.util.constantenum.QueryConditionSymbolEnum;

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
        dynamicTableManage.setPrimaryKeyColumnId(0);//默认编写关联ID为0，后面插入数据后更新该值
        dynamicTableManageMapper.insert(dynamicTableManage);
        //-----------默认查数据-----------------
        //插入列记录
        DynamicColumnManage idColumn = new DynamicColumnManage();
        idColumn.setTableId(dynamicTableManage.getId());
        idColumn.setColumnName("id");
        idColumn.setTypeForMysql(C_MysqlColumnType.INTEGER);
        idColumn.setLength(10);
        idColumn.setNullable(false);
        idColumn.setIsPrimaryKey(true);
        idColumn.setIsAutoincrement(true);
        idColumn.setIsQueryCondition(false);
        idColumn.setIsAllowUpdate(false);
        idColumn.setIsListDisplay(false);
        idColumn.setRemark("主键Id 系统新增表时自动添加");
        idColumn.setIsSystemField(true);
        idColumn.setCreateTime(dynamicTableManage.getCreateTime());
        idColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        idColumn.setFieldSerialNumber(0);
        dynamicColumnManageMapper.insert(idColumn);
        
        //创建时间、创建用户、修改时间、修改用户、删除时间、删除用户、状态（0：无效，1：有效）  默认字段
        //创建用户 默认字段
        DynamicColumnManage createUserIdColumn = new DynamicColumnManage();
        createUserIdColumn.setTableId(dynamicTableManage.getId());
        createUserIdColumn.setColumnName("create_user_id");
        createUserIdColumn.setTypeForMysql(C_MysqlColumnType.INTEGER);
        createUserIdColumn.setLength(10);
        createUserIdColumn.setNullable(false);
        createUserIdColumn.setIsPrimaryKey(false);
        createUserIdColumn.setIsAutoincrement(false);
        createUserIdColumn.setIsQueryCondition(false);
        createUserIdColumn.setIsAllowUpdate(false);
        createUserIdColumn.setIsListDisplay(false);
        createUserIdColumn.setRemark("创建用户ID");
        createUserIdColumn.setIsSystemField(true);
        createUserIdColumn.setCreateTime(dynamicTableManage.getCreateTime());
        createUserIdColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        createUserIdColumn.setFieldSerialNumber(93);
        dynamicColumnManageMapper.insert(createUserIdColumn);
        //创建时间  默认字段 
        DynamicColumnManage createTimeColumn = new DynamicColumnManage();
        createTimeColumn.setTableId(dynamicTableManage.getId());
        createTimeColumn.setColumnName("create_time");
        createTimeColumn.setTypeForMysql(C_MysqlColumnType.DATETIME);
        createTimeColumn.setNullable(false);
        createTimeColumn.setIsPrimaryKey(false);
        createTimeColumn.setIsAutoincrement(false);
        createTimeColumn.setIsQueryCondition(false);
        createTimeColumn.setIsAllowUpdate(false);
        createTimeColumn.setIsListDisplay(false);
        createTimeColumn.setRemark("创建时间");
        createTimeColumn.setIsSystemField(true);
        createTimeColumn.setCreateTime(dynamicTableManage.getCreateTime());
        createTimeColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        createTimeColumn.setFieldSerialNumber(94);
        dynamicColumnManageMapper.insert(createTimeColumn);
        //修改用户 默认字段
        DynamicColumnManage updateUserIdColumn = new DynamicColumnManage();
        updateUserIdColumn.setTableId(dynamicTableManage.getId());
        updateUserIdColumn.setColumnName("update_user_id");
        updateUserIdColumn.setTypeForMysql(C_MysqlColumnType.INTEGER);
        updateUserIdColumn.setLength(10);
        updateUserIdColumn.setNullable(true);
        updateUserIdColumn.setIsPrimaryKey(false);
        updateUserIdColumn.setIsAutoincrement(false);
        updateUserIdColumn.setIsQueryCondition(false);
        updateUserIdColumn.setIsAllowUpdate(false);
        updateUserIdColumn.setIsListDisplay(false);
        updateUserIdColumn.setRemark("修改用户ID");
        updateUserIdColumn.setIsSystemField(true);
        updateUserIdColumn.setCreateTime(dynamicTableManage.getCreateTime());
        updateUserIdColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        updateUserIdColumn.setFieldSerialNumber(95);
        dynamicColumnManageMapper.insert(updateUserIdColumn);
        //修改时间  默认字段 
        DynamicColumnManage updateTimeColumn = new DynamicColumnManage();
        updateTimeColumn.setTableId(dynamicTableManage.getId());
        updateTimeColumn.setColumnName("update_time");
        updateTimeColumn.setTypeForMysql(C_MysqlColumnType.DATETIME);
        updateTimeColumn.setNullable(true);
        updateTimeColumn.setIsPrimaryKey(false);
        updateTimeColumn.setIsAutoincrement(false);
        updateTimeColumn.setIsQueryCondition(false);
        updateTimeColumn.setIsAllowUpdate(false);
        updateTimeColumn.setIsListDisplay(false);
        updateTimeColumn.setRemark("修改时间");
        updateTimeColumn.setIsSystemField(true);
        updateTimeColumn.setCreateTime(dynamicTableManage.getCreateTime());
        updateTimeColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        updateTimeColumn.setFieldSerialNumber(96);
        dynamicColumnManageMapper.insert(updateTimeColumn);
        //删除用户 默认字段
        DynamicColumnManage deleteUserIdColumn = new DynamicColumnManage();
        deleteUserIdColumn.setTableId(dynamicTableManage.getId());
        deleteUserIdColumn.setColumnName("delete_user_id");
        deleteUserIdColumn.setTypeForMysql(C_MysqlColumnType.INTEGER);
        deleteUserIdColumn.setLength(10);
        deleteUserIdColumn.setNullable(true);
        deleteUserIdColumn.setIsPrimaryKey(false);
        deleteUserIdColumn.setIsAutoincrement(false);
        deleteUserIdColumn.setIsQueryCondition(false);
        deleteUserIdColumn.setIsAllowUpdate(false);
        deleteUserIdColumn.setIsListDisplay(false);
        deleteUserIdColumn.setRemark("删除用户ID");
        deleteUserIdColumn.setIsSystemField(true);
        deleteUserIdColumn.setCreateTime(dynamicTableManage.getCreateTime());
        deleteUserIdColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        deleteUserIdColumn.setFieldSerialNumber(97);
        dynamicColumnManageMapper.insert(deleteUserIdColumn);
        //删除时间  默认字段 
        DynamicColumnManage deleteTimeColumn = new DynamicColumnManage();
        deleteTimeColumn.setTableId(dynamicTableManage.getId());
        deleteTimeColumn.setColumnName("delete_time");
        deleteTimeColumn.setTypeForMysql(C_MysqlColumnType.DATETIME);
        deleteTimeColumn.setNullable(true);
        deleteTimeColumn.setIsPrimaryKey(false);
        deleteTimeColumn.setIsAutoincrement(false);
        deleteTimeColumn.setIsQueryCondition(false);
        deleteTimeColumn.setIsAllowUpdate(false);
        deleteTimeColumn.setIsListDisplay(false);
        deleteTimeColumn.setRemark("删除时间");
        deleteTimeColumn.setIsSystemField(true);
        deleteTimeColumn.setCreateTime(dynamicTableManage.getCreateTime());
        deleteTimeColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        deleteTimeColumn.setFieldSerialNumber(98);
        dynamicColumnManageMapper.insert(deleteTimeColumn);
        //状态 默认字段
        DynamicColumnManage stateColumn = new DynamicColumnManage();
        stateColumn.setTableId(dynamicTableManage.getId());
        stateColumn.setColumnName("state");
        stateColumn.setTypeForMysql(C_MysqlColumnType.VARCHAR);
        stateColumn.setLength(4);
        stateColumn.setNullable(false);
        stateColumn.setIsPrimaryKey(false);
        stateColumn.setIsAutoincrement(false);
        stateColumn.setIsQueryCondition(false);
        stateColumn.setQueryConditionSymbol(QueryConditionSymbolEnum.EQUAL.getName());
        stateColumn.setIsAllowUpdate(false);
        stateColumn.setIsListDisplay(false);
        stateColumn.setRemark("状态（1：有效，0：无效）");
        stateColumn.setIsSystemField(true);
        stateColumn.setCreateTime(dynamicTableManage.getCreateTime());
        stateColumn.setCreateUserId(dynamicTableManage.getCreateUserId());
        stateColumn.setFieldSerialNumber(99);
        dynamicColumnManageMapper.insert(stateColumn);
        //-----------默认数据------end -----------
        
        //更新关联
        dynamicTableManage.setPrimaryKeyColumnId(idColumn.getId());
        dynamicTableManageMapper.updateByPrimaryKeySelective(dynamicTableManage);

        //创建表
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE "+dynamicTableManage.getTableName()+" (");
        sql.append(" "+idColumn.getColumnName()+"  "+idColumn.getTypeForMysql()+"("+idColumn.getLength()+") NOT NULL AUTO_INCREMENT COMMENT '"+idColumn.getRemark()+"' , ");
        //创建时间、创建用户、修改时间、修改用户、删除时间、删除用户、状态（0：无效，1：有效）  默认字段
        sql.append(" "+createUserIdColumn.getColumnName()+"  "+createUserIdColumn.getTypeForMysql()+"("+createUserIdColumn.getLength()+") "+(createUserIdColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+createUserIdColumn.getRemark()+"' , ");
        sql.append(" "+createTimeColumn.getColumnName()+"  "+createTimeColumn.getTypeForMysql()+(createTimeColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+createTimeColumn.getRemark()+"' , ");
        sql.append(" "+updateUserIdColumn.getColumnName()+"  "+updateUserIdColumn.getTypeForMysql()+"("+updateUserIdColumn.getLength()+") "+(updateUserIdColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+updateUserIdColumn.getRemark()+"' , ");
        sql.append(" "+updateTimeColumn.getColumnName()+"  "+updateTimeColumn.getTypeForMysql()+(updateTimeColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+updateTimeColumn.getRemark()+"' , ");
        sql.append(" "+deleteUserIdColumn.getColumnName()+"  "+deleteUserIdColumn.getTypeForMysql()+"("+deleteUserIdColumn.getLength()+") "+(deleteUserIdColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+deleteUserIdColumn.getRemark()+"' , ");
        sql.append(" "+deleteTimeColumn.getColumnName()+"  "+deleteTimeColumn.getTypeForMysql()+(deleteTimeColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+deleteTimeColumn.getRemark()+"' , ");
        sql.append(" "+stateColumn.getColumnName()+"  "+stateColumn.getTypeForMysql()+"("+stateColumn.getLength()+") "+(stateColumn.getNullable()?" NULL":" NOT NULL")+" COMMENT '"+stateColumn.getRemark()+"' , ");
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

	public List<DynamicTableManage> queryDynamicTableManageByModel(DynamicTableManage dynamicTableManage){
		return dynamicTableManageMapper.select(dynamicTableManage);
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

	@Override
	public DynamicTableManage queryByTableName(String tableName) {
		DynamicTableManage query = new DynamicTableManage();
		query.setTableName(tableName);
		return dynamicTableManageMapper.selectOne(query);
	}
}
