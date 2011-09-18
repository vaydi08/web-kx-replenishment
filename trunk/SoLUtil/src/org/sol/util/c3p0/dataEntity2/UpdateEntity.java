package org.sol.util.c3p0.dataEntity2;

import java.util.List;

public class UpdateEntity extends Entity{

	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		String idname = criteria.getIdname();
		
		// 加上表名
		sql.append("update ").append(getTablename()).append(" set ");
		
		// 拼合set
		List<CriteriaCondition> list = criteria.getConditionList();

		for(CriteriaCondition condition : list) 
			if(!condition.getName().equals(idname))
				sql.append(condition.getName()).append(condition.getType()).append(',');
		
		sql.deleteCharAt(sql.length() - 1);
		// 设置where id
		sql.append(" where ").append(idname).append("=?");
				
		super.sql = sql.toString();
	}

	@Override
	public String getFullSql() {
		buildSql();
		return sql;
	}

}
