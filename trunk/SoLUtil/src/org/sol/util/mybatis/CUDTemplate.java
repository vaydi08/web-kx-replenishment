package org.sol.util.mybatis;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class CUDTemplate<T extends MyBatisPojo> {
	public String insert(T obj) {
		BEGIN();
		
		INSERT_INTO(obj.tablename());
		obj.caculationColumnList();
		VALUES(obj.returnInsertColumnsName(), obj.returnInsertColumnsDefine());

		return SQL();
	}
	
	public String update(T obj) {
		String idname = obj.id();
		
		BEGIN();
		
		UPDATE(obj.tablename());
		obj.caculationColumnList();
		SET(obj.returnUpdateSet());
		WHERE(idname + "=#{" + idname + "}");
		
		return SQL();
	}
	
	public String delete(T obj) {
		String idname = obj.id();
		
		BEGIN();
		
		DELETE_FROM(obj.tablename());
		WHERE(idname + "=#{" + idname + "}");
		
		return SQL();
	}
}
