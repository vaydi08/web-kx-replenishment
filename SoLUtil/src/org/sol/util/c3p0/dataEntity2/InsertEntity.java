package org.sol.util.c3p0.dataEntity2;

import java.util.List;

public class InsertEntity extends Entity{

	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		// 加上表名
		String idname = criteria.getIdname();
		sql.append("insert into ").append(getTablename()).append("(");
		
		List<CriteriaCondition> list = criteria.getConditionList();
		
		StringBuilder values = new StringBuilder();
		for(CriteriaCondition condition : list) {
			if(!condition.getName().equals(idname)) {
				sql.append(condition.getName()).append(',');
				values.append("?,");
			}
		}
		
		sql.deleteCharAt(sql.length() - 1);
		values.deleteCharAt(values.length() - 1);
		
		sql.append(") values(");
		
		sql.append(values.toString());
		
		sql.append(")");
		
		super.sql = sql.toString();
	}

	@Override
	public String getFullSql() {
		buildSql();
		return sql;
	}

}
