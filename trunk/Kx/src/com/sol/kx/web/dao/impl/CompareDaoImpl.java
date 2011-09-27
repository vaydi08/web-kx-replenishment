package com.sol.kx.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.sol.util.c3p0.DataConsoleAnnotation;
import org.sol.util.common.StringUtil;
import org.sol.util.log.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.CompareDao;
import com.sol.kx.web.dao.pojo.Compare;

@Repository
@Scope("prototype")
public class CompareDaoImpl implements CompareDao{
	private Log log = Logger.SERVICE;
	
	private DataConsoleAnnotation dc;
	
	private Connection connection;
	
	private String tablename;
	
	private void setTablename() {
		this.tablename = "stock" + StringUtil.formatDateCompactNow();
	}
	
	public CompareDaoImpl() {
		dc = new DataConsoleAnnotation(Constants.DB, Constants.TRANS_TIMEOUT);
		setTablename();
	}
	
	public void startTransaction() throws SQLException {
		connection = dc.getConnection();
		connection.setAutoCommit(false);
	}
	
	public void commit() throws SQLException {
		connection.commit();
	}
	
	public void rollback() throws SQLException {
		connection.rollback();
	}
	
	public void close(){
		dc.close();
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
		}
	}
	
	@Value("${sql.compare.supply.tempTable}")
	private String SQL_COMPARE_SUPPLY_TEMPTABLE;
	
	public void createSupplyTempTable() throws SQLException {
		String sql = SQL_COMPARE_SUPPLY_TEMPTABLE.replace(":tablename", tablename);
		log.debug("Update:" + sql);
		
		PreparedStatement ps = connection.prepareStatement(sql);
		try {
			ps.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(ps != null)
				ps.close();
		}
	}
	
	@Value("${sql.compare.supply.insertTemp}")
	private String SQL_COMPARE_SUPPLY_INSERT;
	
	public void insertSupplyTempTable(String pcode,Double weight) throws SQLException {
		String sql = SQL_COMPARE_SUPPLY_INSERT.replace(":tablename", tablename);
		log.ldebug("Update:" + sql,pcode,weight);
		
		PreparedStatement ps = connection.prepareStatement(sql);
		try {
			ps.setString(1, pcode);
			ps.setDouble(2, weight);
			
			ps.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(ps != null)
				ps.close();
		}
	}
	
	@Value("${sql.compare.supply}")
	private String SQL_COMPARE_SUPPLY;
	
	public List<Compare> compareSupply(int shopid,int stocktype) throws Exception {
		String sql = SQL_COMPARE_SUPPLY.replace(":tablename", tablename);
		log.ldebug("Query:" + sql,shopid,stocktype);
		
		PreparedStatement ps = connection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ps.setQueryTimeout(400);
		
		ps.setInt(1, shopid);
		ps.setInt(2, stocktype);
		
		ResultSet rs = null;
		
		List<Compare> list = null;
		
		try {
			rs = ps.executeQuery();
			
			list = new ArrayList<Compare>();
			
			while(rs.next()) {
				Compare obj = new Compare();
				obj.setPname(rs.getString(1));
				obj.setPcode(rs.getString(2));
				obj.setPid(rs.getInt(3));
				obj.setMinweight(rs.getDouble(4));
				obj.setMaxweight(rs.getDouble(5));
				obj.setStock(rs.getInt(6));
				obj.setKucun(rs.getInt(7));
				obj.setNeed(rs.getInt(8));
				
				list.add(obj);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		
		return list;
	}
	
	@Value("${sql.compare.supply.removeTempTable}")
	private String SQL_REMOVE_SUPPLY_TEMPTABLE;
	
	public void removeTempTable() throws SQLException {
		String sql = SQL_REMOVE_SUPPLY_TEMPTABLE.replace(":tablename", tablename);
		log.debug("Drop:" + sql);
		
		PreparedStatement ps = connection.prepareStatement(sql);
		try {
			ps.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(ps != null)
				ps.close();
		}
	}
}
