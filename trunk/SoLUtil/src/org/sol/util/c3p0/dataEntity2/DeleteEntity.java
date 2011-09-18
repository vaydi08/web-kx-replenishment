package org.sol.util.c3p0.dataEntity2;

public class DeleteEntity extends Entity{

	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		// 加上表名
		sql.append("delete from ").append(getTablename()).append(" where ");
		
		// 拼合where条件
		sql.append(criteria.getIdname()).append("=?");
		
		super.sql = sql.toString();	
	}

	@Override
	public String getFullSql() {
		buildSql();
		return sql;
	}

}
