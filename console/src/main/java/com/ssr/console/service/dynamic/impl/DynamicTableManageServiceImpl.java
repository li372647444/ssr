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
import com.ssr.base.util.constant.C_MsqlColumnType;
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
	
	public DynamicTableManage saveDynamicTableManage(DynamicTableManage dynamicTableManage, boolean saveSelective) {
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
        int dynamicTableManageId = dynamicTableManageMapper.insertSelective(dynamicTableManage);
        dynamicTableManage.setId(dynamicTableManageId);
        //插入列记录
        DynamicColumnManage idColumn = new DynamicColumnManage();
        idColumn.setTableId(dynamicTableManage.getId());
        idColumn.setColumnName("id");
        idColumn.setTypeForMysql(C_MsqlColumnType.INTEGER);
        idColumn.setLength(10);
        //以下3条必须如下设置
        idColumn.setNullable(false);
        idColumn.setIsPrimaryKey(true);
        idColumn.setIsAutoincrement(true);
        idColumn.setRemark("主键Id 系统新增表时自动添加");
        idColumn.setCreateTime(now);
        int idColumnId = dynamicColumnManageMapper.insertSelective(idColumn);
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
        dynamicTableManageMapper.createNewDynamicTable(paraMap);

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
}
