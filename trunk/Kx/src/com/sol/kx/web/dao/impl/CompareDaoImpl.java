package com.sol.kx.web.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
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
import com.sol.kx.web.dao.pojo.CargoCompare;
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
	
	private int updateSql(String sql,Object... objs) throws SQLException {
		sql = sql.replace(":tablename", tablename);
		log.ldebug("Update:" + sql,objs);
		
		PreparedStatement ps = connection.prepareStatement(sql);
		try {
			if(objs != null && objs.length > 0)
				for(int i = 0; i < objs.length; i ++)
					ps.setObject(i + 1, objs[i]);
			
			return ps.executeUpdate();
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
	
	@Value("${sql.compare.cargo.updatestock}")
	private String SQL_CARGO_STOCK_UPDATE;
	
	public void cargoStockUpdate(String shopname,String serial,Double weight) throws SQLException {
		updateSql(SQL_CARGO_STOCK_UPDATE, shopname,serial,weight);
	}
	
	// cargo compare
	@Value("${sql.compare.cargo.createtb}")
	private String SQL_CARGO_COMPARE_CREATETB;
	
	public void cargoCompareCreateTb(Integer minallot) throws SQLException {
		updateSql(SQL_CARGO_COMPARE_CREATETB,minallot,minallot);
	}
	
	@Value("${sql.compare.cargo.findstock}")
	private String SQL_CARGO_COMPARE_FINDSTOCK;
	
	public List<CargoCompare> cargoFindStock() throws SQLException {
		String sql = SQL_CARGO_COMPARE_FINDSTOCK.replace(":tablename", tablename);
		log.ldebug("Query:" + sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			List<CargoCompare> list = new LinkedList<CargoCompare>();
			while(rs.next()) {
				CargoCompare cargoCompare = new CargoCompare(
						rs.getInt(1),rs.getString(2),rs.getString(3),
						rs.getString(4),rs.getInt(5),rs.getDouble(6),
						rs.getString(7));
				list.add(cargoCompare);
			}
			
			return list;
		} catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
	}
	
	@Value("${sql.compare.cargo.updatetb}")
	private String SQL_CARGO_COMPARE_UPDATETB;
	
	public Object[] cargoCompareUpdateTb(CargoCompare cargoCompare) throws SQLException {
		String sql = SQL_CARGO_COMPARE_UPDATETB.replace(":tablename", tablename);
		log.ldebug("Query:" + sql,cargoCompare.getPid(),cargoCompare.getWeight(),cargoCompare.getSerial(),cargoCompare.getNum());
		
		CallableStatement cs = null;
		ResultSet rs = null;
		
		try {
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.REAL);
			cs.setString(2,Integer.toString(cargoCompare.getPid()));
			cs.setString(3, cargoCompare.getWeight().toString());
			cs.setString(4, cargoCompare.getSerial() + ",");
			cs.setString(5, Integer.toString(cargoCompare.getNum()));
			rs = cs.executeQuery();
			
			if(rs.next()) {
				Integer rowcount = rs.getInt(1);
				String shopname = rs.getString(2);
				
				return new Object[]{rowcount,shopname};
			}
			
			return null;
		} catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null)
				rs.close();
			if(cs != null)
				cs.close();
		}
	}
	
	@Value("${sql.compare.cargo.updatetb_normal}")
	private String SQL_CARGO_COMPARE_UPDATE_NORMAL;
	
	public Object[] cargoCompareUpdateTbNormal(CargoCompare cargoCompare) throws SQLException {
		String sql = SQL_CARGO_COMPARE_UPDATE_NORMAL.replace(":tablename", tablename);
		log.ldebug("Query:" + sql);
		
		CallableStatement cs = null;
		ResultSet rs = null;
		
		try {
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.REAL);
			cs.setString(2,Integer.toString(cargoCompare.getPid()));
			cs.setString(3, cargoCompare.getWeight().toString());
			cs.setString(4, cargoCompare.getSerial() + ",");
			cs.setString(5, Integer.toString(cargoCompare.getNum()));
			rs = cs.executeQuery();
			
			if(rs.next()) {
				Integer rowcount = rs.getInt(1);
				String shopname = rs.getString(2);
				
				return new Object[]{rowcount,shopname};
			}
			
			return null;
		} catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null)
				rs.close();
			if(cs != null)
				cs.close();
		}
	}
	
	@Value("${sql.compare.cargo.result}")
	private String SQL_CARGO_COMPARE_RESULT;
	
	public List<CargoCompare> cargoFindResult() throws SQLException {
		String sql = SQL_CARGO_COMPARE_RESULT.replace(":tablename", tablename);
		log.ldebug("Query:" + sql);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			List<CargoCompare> list = new ArrayList<CargoCompare>(100);
			while(rs.next()) {
				CargoCompare cargoCompare = new CargoCompare();
				cargoCompare.setId(rs.getInt(1));
				cargoCompare.setPname(rs.getString(2));
				cargoCompare.setPid(rs.getInt(3));
				cargoCompare.setPcode(rs.getString(4));
				cargoCompare.setShopname(rs.getString(5));
				cargoCompare.setShopid(rs.getInt(6));
				cargoCompare.setMinweight(rs.getDouble(7));
				cargoCompare.setMaxweight(rs.getDouble(8));
				cargoCompare.setStock(rs.getInt(9));
				cargoCompare.setStocknow(rs.getInt(10));
				cargoCompare.setSaletime(rs.getTimestamp(11));
				cargoCompare.setSerial(rs.getString(12));
				cargoCompare.setNum(rs.getInt(13));
				cargoCompare.setMinallot(rs.getInt(14));
				
				list.add(cargoCompare);
			}
			
			return list;
		} catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
	}
}
