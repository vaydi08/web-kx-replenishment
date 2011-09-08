package org.sol.util.c3p0.dataEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


/**
 * 2011-08-28 通过pojo生成SQL和参数列表
 * @author sol
 *
 */
public class UpdateDataEntity extends DataEntity{
	
	public UpdateDataEntity(Class<?> clazz) {
		super(clazz);
	}
	
	public UpdateDataEntity(Object obj) throws Exception {
		super(obj.getClass());
		
		buildConditionMap(obj);
	}

	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		String idname = getIdname();
		if(idname == null)
			throw new RuntimeException("主键未设置");
		Object[] idvalue = conditionMap.get(idname + "=?");
		if(idvalue == null || idvalue[0] == null)
			throw new RuntimeException("主键未设置");
		
		// 加上表名
		sql.append("update ").append(tablename).append(" set ");
		
		// 拼合set
		buildSet(sql,idname);
		
		// 设置where id
		sql.append(" where ").append(idname).append("=?");
		
		this.params.add(idvalue[0]);
		
		super.sql = sql.toString();
	}
	
	protected void buildSet(StringBuilder sql,String idname) {
		// 拼合where条件
		if(conditionMap.size() > 0) {
			// 查询参数列表
			List<Object> list = new ArrayList<Object>(conditionMap.size());
			for(Entry<String,Object[]> en : conditionMap.entrySet()) {
				if(en.getValue() != null && !en.getKey().equals(idname + "=?")) {
					sql.append(en.getKey()).append(",");
					
					for(Object obj : en.getValue())
						list.add(obj);
				}
			}

			sql.deleteCharAt(sql.length() - 1);
			this.params = list;
		}
	}
}
