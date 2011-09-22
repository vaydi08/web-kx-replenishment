package org.sol.util.c3p0.dataEntity;


/**
 * 2011-08-28 通过pojo生成SQL和参数列表
 * @author sol
 *
 */
public class CountDataEntity extends DataEntity{
	
	public CountDataEntity(Class<?> clazz) {
		super(clazz);
	}
	
	public CountDataEntity(Object obj) throws Exception {
		super(obj.getClass());
		
		buildConditionMap(obj);
	}

	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		String idname = getIdname();
		if(idname == null)
			idname = "*";
		
		// 加上表名
		sql.append("select count(").append(idname).append(") from ").append(tablename);
		
		// 拼合where条件
		sql.append(getWhereSql());
		
		super.sql = sql.toString();
	}
}
