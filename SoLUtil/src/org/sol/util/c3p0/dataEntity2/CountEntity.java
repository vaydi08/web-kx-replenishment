package org.sol.util.c3p0.dataEntity2;

public class CountEntity extends Entity{

	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		String idname = getIdname();
		if(idname == null)
			idname = "*";
		
		// 加上表名
		sql.append("select count(").append(idname).append(") from ").append(getTablename());
				
		super.sql = sql.toString();
	}

	@Override
	public String getFullSql() {
		buildSql();
		return sql + criteria.getWhereSql();
	}

}
