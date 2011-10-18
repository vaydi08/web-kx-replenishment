package com.sol.kx.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
		this.tablename = StringUtil.formatDateCompactNow();
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
	
	
	// CARGO
	
	private void updateSql(String sql,Object... objs) throws SQLException {
		sql = sql.replace(":tablename", tablename);
		log.debug("Update:" + sql);
		
		PreparedStatement ps = connection.prepareStatement(sql);
		try {
			if(objs != null && objs.length > 0)
				for(int i = 0; i < objs.length; i ++)
					ps.setObject(i + 1, objs[i]);
			
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(ps != null)
				ps.close();
		}
	}
	
	// cargo supply
	@Value("${sql.compare.cargo.supply.temp}")
	private String SQL_CARGO_SUPPLY_TEMP;
	
	public void cargoSupplyCreateTempTable(Integer stocktype) throws SQLException {
		updateSql(SQL_CARGO_SUPPLY_TEMP,stocktype);
	}
	
	@Value("${sql.compare.cargo.supply.removetemp}")
	private String SQL_CARGO_SUPPLY_REMOVETEMP;
	
	public void cargoSupplyRemoveTempTable() throws SQLException {
		updateSql(SQL_CARGO_SUPPLY_REMOVETEMP);
	}
	
	@Value("${sql.compare.cargo.supply.update}")
	private String SQL_CARGO_SUPPLY_DATAUPDATE;
	
	public void cargoSupplyDataUpdate(String pcode,String shopname,Double weight) throws SQLException {
		updateSql(SQL_CARGO_SUPPLY_DATAUPDATE, shopname,pcode,weight);
	}
	
	// cargo sale
	@Value("${sql.compare.cargo.sale.temp}")
	private String SQL_CARGO_SALE_TEMP;
	
	public void cargoSaleCreateTempTable() throws SQLException {
		updateSql(SQL_CARGO_SALE_TEMP);
	}
	
	@Value("${sql.compare.cargo.sale.removetemp}")
	private String SQL_CARGO_SALE_REMOVETEMP;
	
	public void cargoSaleRemoveTempTable() throws SQLException {
		updateSql(SQL_CARGO_SALE_REMOVETEMP);
	}
	
	@Value("${sql.compare.cargo.sale.update}")
	private String SQL_CARGO_SALE_DATAUPDATE;
	
	public void cargoSaleDataUpdate(String serial,Integer sale,long saletime,String shopname,String pcode,Double weight,Integer stocktype) throws SQLException {
		updateSql(SQL_CARGO_SALE_DATAUPDATE, serial,sale,new Timestamp(saletime),shopname,pcode,stocktype,weight);
	}
	
	@Value("${sql.compare.cargo.sale.delete}")
	private String SQL_CARGO_SALE_DELTE;
	
	public void cargoSaleDateDelete(String serial) throws SQLException {
		updateSql(SQL_CARGO_SALE_DELTE, serial);
	}
	
	// cargo stock
	@Value("${sql.compare.cargo.stock.temp}")
	private String SQL_CARGO_STOCK_TEMP;
	
	public void cargoStockCreateTempTable() throws SQLException {
		updateSql(SQL_CARGO_STOCK_TEMP);
	}
	
	@Value("${sql.compare.cargo.stock.removetemp}")
	private String SQL_CARGO_STOCK_REMOVETEMP;
	
	public void cargoStockRemoveTempTable() throws SQLException {
		updateSql(SQL_CARGO_STOCK_REMOVETEMP);
	}
	
	@Value("${sql.compare.cargo.stock.update}")
	private String SQL_CARGO_STOCK_DATAUPDATE;
	
	public void cargoStockDataUpdate(String serial,Integer num,String pcode,Double weight) throws SQLException {
		updateSql(SQL_CARGO_STOCK_DATAUPDATE, serial,num,weight,pcode);
	}
	
}
