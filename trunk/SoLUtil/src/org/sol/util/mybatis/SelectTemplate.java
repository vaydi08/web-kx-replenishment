package org.sol.util.mybatis;

import java.util.ArrayList;
import java.util.List;

public class SelectTemplate<T extends MyBatisPojo>{

	public String select(T obj) {
		return select(obj,true);
	}
	
	public String select(T obj,boolean islike) {
		org.apache.ibatis.jdbc.SelectBuilder.BEGIN();
		
		org.apache.ibatis.jdbc.SelectBuilder.SELECT("*");
		org.apache.ibatis.jdbc.SelectBuilder.FROM(obj.tablename());
		
		for(MyBatisPojo.WhereColumn column : obj.returnWhereColumnsName()) {
			if(column.isString && islike)
				org.apache.ibatis.jdbc.SelectBuilder.WHERE(column.name + " like '%'+#{" +column.name + "}+'%'");
			else
				org.apache.ibatis.jdbc.SelectBuilder.WHERE(column.name + "=#{" + column.name + "}");
		}
		
		return org.apache.ibatis.jdbc.SelectBuilder.SQL();
	}
	
	public String count(T obj) {
		return count(obj,true);
	}
	
	public String count(T obj,boolean islike) {
		org.apache.ibatis.jdbc.SelectBuilder.BEGIN();
		
		org.apache.ibatis.jdbc.SelectBuilder.SELECT(" count(" + obj.id() + ")");
		org.apache.ibatis.jdbc.SelectBuilder.FROM(obj.tablename());
		
		for(MyBatisPojo.WhereColumn column : obj.returnWhereColumnsName()) {
			if(column.isString && islike)
				org.apache.ibatis.jdbc.SelectBuilder.WHERE(column.name + " like '%'+#{" +column.name + "}+'%'");
			else
				org.apache.ibatis.jdbc.SelectBuilder.WHERE(column.name + "=#{" + column.name + "}");
		}
		
		return org.apache.ibatis.jdbc.SelectBuilder.SQL();
	}
	
	public String get(T obj) {
		String tablename = obj.tablename();
		String idname = obj.id();
		return "select * from " + tablename + " where " + idname + "=#{" + idname + "}";
	}
	
	public String selectByPage(T obj) {
		return selectByPage(obj, obj.getPage(), obj.getPageSize(),"*",true);
	}
	
//	public String selectByPage(T obj,int page,int pageSize) {
//		return selectByPage(obj, page, pageSize,"*",true);
//	}
	
	public String selectByPage(T obj,int page,int pageSize,String selectColumns,boolean islike) {
		BEGIN();
		
		String tablename = obj.tablename();
		String idname = obj.id();
		
		SELECT(selectColumns);
		FROM(tablename);
		
		List<MyBatisPojo.WhereColumn> columnList = obj.returnWhereColumnsName();
		
		for(MyBatisPojo.WhereColumn column : columnList) {
			if(column.isString && islike)
				WHERE(column.name + " like '%'+#{" +column.name + "}+'%'");
			else
				WHERE(column.name + "=#{" + column.name + "}");
		}
		ORDER_BY(idname + " desc");
				
		StringBuilder sb = new StringBuilder();
		if(page > 1) {
			sb.append("declare @id nvarchar(100)\n");
			sb.append("select @id=min(").append(idname).append(") from (select top ")
			.append(page * pageSize - pageSize).append(" ").append(idname).append(" from ").append(tablename);
			
			selectClause(sb, "WHERE", sql().where, "(", ")", " AND ");
			
			sb.append(" order by ").append(idname).append(" desc) tb1\n");
		}
		sb.append("set ROWCOUNT ").append(pageSize).append("\n");
		
		if(page > 1) {
			WHERE(idname + "<@id");
			
		}
		
		String sql = SQL();
		sb.append(sql);
//		if(page > 1)
//			sb.append((sql.contains("where")) ? " and " :" where ").append(idname).append("<@id");
//		sb.append(" order by ").append(idname).append(" desc\n");
		sb.append("\nset ROWCOUNT 0");

		RESET();
		
		return sb.toString();
	}
	
	
	private static class SelectSQL {

		List select;
		List from;
		List where;
		List having;
		List groupBy;
		List orderBy;
		List lastList;

		private SelectSQL() {
			select = new ArrayList();
			from = new ArrayList();
			where = new ArrayList();
			having = new ArrayList();
			groupBy = new ArrayList();
			orderBy = new ArrayList();
			lastList = new ArrayList();
		}

	}

	public static void BEGIN() {
		RESET();
	}

	public static void RESET() {
		localSQL.set(new SelectSQL());
	}

	public static void SELECT(String columns) {
		sql().select.add(columns);
	}

	public static void FROM(String table) {
		sql().from.add(table);
	}

	public static void WHERE(String conditions) {
		sql().where.add(conditions);
		sql().lastList = sql().where;
	}

	public static void OR() {
		sql().lastList.add(") \nOR (");
	}

	public static void AND() {
		sql().lastList.add(") \nAND (");
	}

	public static void GROUP_BY(String columns) {
		sql().groupBy.add(columns);
	}

	public static void HAVING(String conditions) {
		sql().having.add(conditions);
		sql().lastList = sql().having;
	}

	public static void ORDER_BY(String columns) {
		sql().orderBy.add(columns);
	}

	public static String SQL()
    {
        String s;
        StringBuilder builder = new StringBuilder();
        selectClause(builder, "SELECT", sql().select, "", "", ", ");
        selectClause(builder, "FROM", sql().from, "", "", ", ");
        selectClause(builder, "WHERE", sql().where, "(", ")", " AND ");
        selectClause(builder, "GROUP BY", sql().groupBy, "", "", ", ");
        selectClause(builder, "HAVING", sql().having, "(", ")", " AND ");
        selectClause(builder, "ORDER BY", sql().orderBy, "", "", ", ");
        s = builder.toString();
//        RESET();
        return s;
    }

	private static void selectClause(StringBuilder builder, String keyword,
			List parts, String open, String close, String conjunction) {
		if (!parts.isEmpty()) {
			if (builder.length() > 0)
				builder.append("\n");
			builder.append(keyword);
			builder.append(" ");
			builder.append(open);
			String last = "________";
			int i = 0;
			for (int n = parts.size(); i < n; i++) {
				String part = (String) parts.get(i);
				if (i > 0 && !part.equals(") \nAND (")
						&& !part.equals(") \nOR (")
						&& !last.equals(") \nAND (")
						&& !last.equals(") \nOR ("))
					builder.append(conjunction);
				builder.append(part);
				last = part;
			}

			builder.append(close);
		}
	}

	private static SelectSQL sql() {
		SelectSQL selectSQL = (SelectSQL) localSQL.get();
		if (selectSQL == null) {
			RESET();
			selectSQL = (SelectSQL) localSQL.get();
		}
		return selectSQL;
	}

	private static final String AND = ") \nAND (";
	private static final String OR = ") \nOR (";
	private static final ThreadLocal localSQL = new ThreadLocal();
}
