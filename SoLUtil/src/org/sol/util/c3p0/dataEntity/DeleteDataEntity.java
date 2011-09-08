package org.sol.util.c3p0.dataEntity;


/**
 * 2011-08-28 通过pojo生成SQL和参数列表
 * @author sol
 *
 */
public class DeleteDataEntity extends DataEntity{
	
	public DeleteDataEntity(Class<?> clazz) {
		super(clazz);
	}
	
	public DeleteDataEntity(Object obj) throws Exception {
		super(obj.getClass());
		
		buildConditionMap(obj);
	}
	
	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		// 加上表名
		sql.append("delete from ").append(tablename);
		
		// 拼合where条件
		buildWhere(sql);
		
		super.sql = sql.toString();
	}

}
