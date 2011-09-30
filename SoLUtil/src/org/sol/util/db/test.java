package org.sol.util.db;

import java.sql.SQLException;
import java.sql.Types;

public class test {
	private IConnectionProvider connectionProvider = null;
	private String sourceName = "megajoysms";
	private int queryTime = 5;
	
	private DataConsole dc;
	
	public test() {
		/*
		 * 首先建立数据源对象,现在演示的使用的是JDBC提供源
		 * 三个参数 1.驱动名称 2.url(数据库名不需要填写,由sourceName参数执行) 3.登录用户名 4.密码
		 * 代码中还提供了一个tomcat的jndi数据提供源,其他类似的源可以直接继承IConnectionProvider接口实现
		 */
		try {
			connectionProvider = new JdbcProvider(
					"com.microsoft.sqlserver.jdbc.SQLServerDriver", 
					"jdbc:sqlserver://10.0.0.44:1433",
					"sa","megajoy");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		dc = new DataConsole(connectionProvider, sourceName, queryTime);
	}
	
	/**
	 * Find 方法用来查询对象
	 * 接收3+ 个参数
	 * 第一个参数表示执行的Select语句
	 * 第二个参数表示查询结果映射到一个对象类型上
	 * 第三个参数表示对查询结果字段的映射关系
	 * 第四个字段用于如果SQL语句是带参数的,从这里开始填入变量列表
	 * 
	 * 数据表test结构
	 * int id,
	 * varchar text,
	 * varchar value
	 * 映射对象结构
	 * class pojo {
		private Integer id;
		private String text;
		get/set 方法...
		}
	 * 第三个参数的类型是Map<String,Class<?>>表示 字段名-字段类型的映射关系
	 * 可以自己建立这个map,map的键用来匹配查询结果集的字段及映射类的字段
	 * 也可以用DataConsole.parseSmap(Class,String...)方法获取map
	 * 这个函数接收两个参数,第一个参数表示映射的类型,第二个开始的参数表示字段名称,函数会在映射类中匹配相应的字段类型
	 * PS:映射类内部的字段类型不能是基础类型比如int,short,要写成Integer,Short
	 */
	public void find() {
		try {
			System.out.println(dc.find(
					"select top 10 * from test where id>?",
					pojo.class,
					DataConsole.parseSmap(pojo.class, "id","whichservice"), 
					10));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行更新语句的方法
	 * 接收1+个参数
	 * 第一个参数表示SQL更新语句
	 * 第二个以后的参数表示传入的变量列表,无参数语句不填这些参数
	 * 返回结果是查询影响的记录条数
	 */
	public void update() {
		try {
			System.out.println(dc.updatePrepareSQL(
					"insert into test(id,text,value) values(?,?,?)", 
					1,"text","value"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行插入动作,并返回这个操作记录的主键,如果操作无主键或主键不是自增型,返回0
	 * 参数类似于更新动作,这里演示了没有sql变量的情况
	 */
	public void insertAndReturnKey() {
		try {
			System.out.println(dc.insertAndReturnKey("insert into test(text,value) values('text','value')"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 类似于执行select语句,此方法用于执行带有返回结果集的存储过程
	 */
	public void callAndReturnResultSet() {
		try {
			System.out.println(dc.call(pojo.class, 
					"{call proc_returnpojo(?)}", 
					DataConsole.parseSmap(pojo.class, "id","whichservice"), 
					1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用于执行存储过程,如果存储过程带有返回值(非output参数),返回接受到的返回值
	 *  首字段用于处理返回值 所以存储过程写法必须是 {?=call PRODUCENAME(?,?...,?)}
	 * 比如:
	 * create proc_testreturn
	 * begin
	 * return 1
	 * end
	 */
	public void callAndReturn() {
		try {
			System.out.println(dc.callWithReturn("{?=call proc_testreturn}", Types.INTEGER));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
