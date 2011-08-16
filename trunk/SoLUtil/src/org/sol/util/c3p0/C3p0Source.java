package org.sol.util.c3p0;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@Repository
public class C3p0Source {
	String sourceName;
	private  HashMap<String,ComboPooledDataSource> dataMap = new HashMap<String,ComboPooledDataSource>();//用来存放数据源
	private  String[] ips, 				   //数据库所在服务器的ip地址
					  users,				   //数据库用户名
					  passwords,			   //数据库密码 
					  databases,			   //所有数据源 
					  databaseNames,		   //所有数据源的名字
						dbdrivers;				//数据源所用的驱动
//	private static C3p0Source instance;	           //生成一个自己的对象 实现单态模式		
	   					//一个用来存放c3p0数据源的集合
	private  ArrayList<String> customDatabaseList;   //存放数源的list集合
	private  DynamicProperties config;			   			//读取属性文件类
	private  int[]      maxpoolsize,				   		//最大连接数
	 					initialpoolsize,			   //初始化连接数
	 					maxidletime,				   //最大空闲时间
	 					minpoolsize,				   //最小连接数
	 					acquireincrement,		   //一次获取的连接数
	 					idleconnectiontestperiod;   //多少时间查看一次空闲的连接
	
	private String URL_PROTOCOL;
	private String CONSOLE_PROPERTIES;
	
	private static final Log log = LogFactory.getLog(C3p0Source.class);
	
	private boolean bResetFlag = false;
	
	/**
	 * 构造器
	 * 
	 */
	public C3p0Source(String URL_PROTOCOL,String CONSOLE_PROPERTIES){
			setURL_PROTOCOL(URL_PROTOCOL);
		try {   
		      log.info("加载dataConnection...");
		      config = new DynamicProperties(CONSOLE_PROPERTIES);
				ips = config.getProperty("ips").split(",");
				users = config.getProperty("users").split(",");
				passwords = config.getProperty("passwords").split(",");
				databases = config.getProperty("databases").split(",");
				databaseNames = config.getProperty("databasenames").split(",");
				dbdrivers = config.getProperty("dbDrivers").split(",");
				maxpoolsize = parseStringToIntArray(config
						.getProperty("maxpoolsize"));
				initialpoolsize = parseStringToIntArray(config
						.getProperty("initialpool"));
				maxidletime = parseStringToIntArray(config
						.getProperty("maxidletime"));
				minpoolsize = parseStringToIntArray(config
						.getProperty("minpoolsize"));
				acquireincrement = parseStringToIntArray(config
						.getProperty("acquireincrement"));
				idleconnectiontestperiod = parseStringToIntArray(config
						.getProperty("idleconnectiontestperiod"));
		      build();
		      customDatabaseList = new ArrayList<String>();
		      
		    } catch (Exception e) {
		    	e.printStackTrace();
		      //数据库配置文件出错
		      log.info("数据库加载配置文件出错");
		      System.exit(1);
		    }
	}
	public C3p0Source() {
		this("jdbc:sqlserver://","/console.properties");
	}
	
	/**
	 * 方法
	 *
	 * 将字符串转换为整型数组
	 */
	private int[] parseStringToIntArray(String  temp){
		String [] tempString=temp.split(",");
		int[] tempInt=new int[tempString.length];
		for(int i=0;i<tempString.length;i++){
			tempInt[i]=Integer.parseInt(tempString[i]);
		}
		return tempInt;
	}

	/**
	 * 方法
	 *
	 * 对数据库连接池进行配置并返回出一个连接池
	 */
	public ComboPooledDataSource setupComboPooledDataSource(String connectURI, String user, String password
			,int maxpoolsize,int initialpoolsize,int maxidletime,int minpoolsize,int acquireincrement
			,int idleconnectiontestperiod,String dbdriver) {
		ComboPooledDataSource ds=new ComboPooledDataSource();
		try{
			//ds.setDriverClass("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			if("sqlserver".equals(dbdriver)){
				ds.setDriverClass("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			}else if("mysql".equals(dbdriver)){
				ds.setDriverClass("com.mysql.jdbc.Driver");
			}else if("oracle".equals(dbdriver)){
				ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
			}else{
				ds.setDriverClass("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			}
			//com.mysql.jdbc.Driver linkUrl = "jdbc:mysql://59.44.60.168:3306/ms_sms";
			ds.setJdbcUrl(connectURI);
			ds.setUser(user);
			ds.setPassword(password);
			ds.setPreferredTestQuery("select 1");//定义所有连接测试都执行的测试语句。
			if("oracle".equals(dbdriver))
				ds.setPreferredTestQuery("select 1 from dual");
			ds.setMaxPoolSize(maxpoolsize);							   //连接池中保留的最大连接数。
			ds.setInitialPoolSize(initialpoolsize);					   //初始化时获取三个连接
			ds.setMaxIdleTime(maxidletime);							   //最大空闲时间,25000秒内未使用则连接被丢弃。若为0则永不丢弃。
			ds.setMinPoolSize(minpoolsize);      					   // 连接池中保留的最小连接数。
			ds.setAcquireIncrement(acquireincrement);				   //当连接池中的连接耗尽的时候c3p0一次同时获取的连接数
			ds.setIdleConnectionTestPeriod(idleconnectiontestperiod);  //每60秒检查所有连接池中的空闲连接
			ds.setAutoCommitOnClose(true);							   //连接关闭时默认将所有未提交的操作回滚
			//ds.setAcquireRetryAttempts(30);						   //定义在从数据库获取新连接失败后重复尝试的次数。
			ds.setAcquireRetryDelay(2000);    //两次连接中间隔时间，单位毫秒。  
			ds.setTestConnectionOnCheckin(true);
			ds.setAcquireRetryAttempts(2);
			
//	    获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
//	    保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
//	    获取连接失败后该数据源将申明已断开并永久关闭。Default: false  breakAfterAcquireFailure
			ds.setBreakAfterAcquireFailure(false);
			//ds.setBreakAfterAcquireFailure(false);
			ds.setNumHelperThreads(10);  
			 // 当连接池用完时客户端调用getConnection()后等待获取新连接的时间,超时后将抛出SQLException,如设为0则无限期等待.单位毫秒,默认为0 
			ds.setCheckoutTimeout(30000); 
		}catch(Exception e){
			log.info("加载数据源时异常");
		}
		return ds;
  }
	/**
	 * 方法
	 *
	 * 在map中加入数据源
	 */
	  public  void build() {
	    try{
	    	for (int i = 0; i < databases.length; i++) {
	    		if("sqlserver".equals(dbdrivers[i])){
		    		dataMap.put(databaseNames[i], setupComboPooledDataSource(URL_PROTOCOL + ips[i] +
	    					";DatabaseName=" + databases[i], users[i], passwords[i],maxpoolsize[0],initialpoolsize[0],maxidletime[0],
	    					minpoolsize[0],acquireincrement[0],idleconnectiontestperiod[0],dbdrivers[0]));
		    		log.info("创建的数据连结为:"+URL_PROTOCOL + ips[i] +";DatabaseName=" + databases[i]);
	    		}else if("mysql".equals(dbdrivers[i])){
		    		dataMap.put(databaseNames[i], setupComboPooledDataSource("jdbc:mysql://" + ips[i] +
	    					"/" + databases[i], users[i], passwords[i],maxpoolsize[0],initialpoolsize[0],maxidletime[0],
	    					minpoolsize[0],acquireincrement[0],idleconnectiontestperiod[0],dbdrivers[0]));
		    		log.info("创建的数据连结为:"+"jdbc:mysql://" + ips[i] +"/" + databases[i]);
	    		}else if("oracle".equals(dbdrivers[i])){
		    		dataMap.put(databaseNames[i], setupComboPooledDataSource("jdbc:oracle:thin:@" + ips[i] +
	    					":" + databases[i], users[i], passwords[i],maxpoolsize[0],initialpoolsize[0],maxidletime[0],
	    					minpoolsize[0],acquireincrement[0],idleconnectiontestperiod[0],dbdrivers[0]));
		    		log.info("创建的数据连结为:"+"jdbc:oracle:thin:@" + ips[i] +":" + databases[i]);
	    		}

	    }
	    }catch(Exception e){
	    	log.error("创建数据库连接池出错",e);
	    }
	  }
	  
	  /**
	   * 方法
	   *
	   * 提供读取自定义业务数据库时使用 将此数据库连接放入map和list集合中
	   */
	    public  void bulid(String databaseName)throws Exception {
	      if (!dataMap.containsKey(databaseName)) {//当map中不包含databaseName这个键所索引的值时
	    	 for(int i =0 ;i<databaseNames.length;i++){
	    		 if(databaseName.equals(databaseNames[i])){
	    			 String url = "";
	    			 if("sqlserver".equals(dbdrivers[i])){
	    				 url = URL_PROTOCOL + ips[i] + ";DatabaseName=" + databaseName;
	    			 }else if("mysql".equals(dbdrivers[i])){
	    				 url = "jdbc:mysql://" + ips[i] + "/" + databaseName;
	    			 }else if("oracle".equals(dbdrivers[i])){
	    				 url = "jdbc:oracle:thin:@" + ips[i] + ":" + databaseName;
	    			 }else{
	    				 url = URL_PROTOCOL + ips[i] + ";DatabaseName=" + databaseName;
	    			 }
	    			  
	   	    	  dataMap.put(databaseName, setupComboPooledDataSource(url, users[i], passwords[i],maxpoolsize[i],initialpoolsize[i],maxidletime[i],
	    					minpoolsize[i],acquireincrement[i],idleconnectiontestperiod[i],dbdrivers[i]));
	   	    	  customDatabaseList.add(databaseName);
	    		 }
	    	 }

	      }
	    }
	  /**
	   * 方法
	   *
	   * 重新创建指定名字的数据源
	   */
	  public  void reBuild(String name)throws Exception {
	    for (int i = 0; i < databaseNames.length; i++) {
	      if (databaseNames[i].equals(name)) {
	        shutdownDataSource(name);
	        dataMap.remove(name);
	        String url = "";
			 if("sqlserver".equals(dbdrivers[i])){
				 url = URL_PROTOCOL + ips[i] + ";DatabaseName=" + databases[i];
			 }else if("mysql".equals(dbdrivers[i])){
				 url = "jdbc:mysql://" + ips[i] + "/" + databases[i];
			 }else if("oracle".equals(dbdrivers[i])){
				 url = "jdbc:oracle:thin:@" + ips[i] + ":" + databases[i];
			 }else{
				 url = URL_PROTOCOL + ips[i] + ";DatabaseName=" + databases[i];
			 }
	        dataMap.put(name, setupComboPooledDataSource(url, users[i], passwords[i],maxpoolsize[i],initialpoolsize[i],maxidletime[i],
	              	    					minpoolsize[i],acquireincrement[i],idleconnectiontestperiod[i],dbdrivers[i]));
	        
	      }
	    }
	  } 
	
	  /**
	 * 方法
	 *
	 * 取得自己本身的对象
	 */
//	public static C3p0Source getInstance() {
//		if (instance == null) {
//			try {
//				instance = new C3p0Source();
//	        } catch (Exception e) {
//	        	e.printStackTrace();
//	        }
//	    }
//	    return instance;
//	}
	/**
	 * 方法
	 *
	 * 判断数据库是否获取成功
	 */
	 public  boolean isValidSource(String sourceName) {
		 
		 try {
			 getConnection(sourceName);
		      return true;
		 } catch (Exception sqle) {
			 log.info("出错！获取数据库连接未成功");
		      return false;
		 }
	}
	/**
	 * 方法
	 *
	 * 关闭所有数据源

	 bingo 2009-9-21
	 */
	  public  void shutdownDataSource() throws Exception {
	    for (int i = 0; i < databases.length; i++) {
	      ((ComboPooledDataSource) dataMap.get(databaseNames[i])).close();
	    }
	    Iterator<String> i = customDatabaseList.iterator();
	    while (i.hasNext()) {
	      ((ComboPooledDataSource) dataMap.get((String) i.next())).close();
	    }
	  }
	/**
	 * 方法
	 *
	 * 关闭指定的连接池

	 bingo 2009-9-21
	 */
	  public  void shutdownDataSource(String name) {
	    try {
	      ((ComboPooledDataSource) dataMap.get(name)).close();
	    } catch (Exception e) {
	    	log.info("关闭数据库连接出错");
	    }
	  }
	
	/**
	 * 方法
	 * 加入了线程锁保证了连接数据库的安全
	 * 从连接池中获取连接
	*/
	public Connection getConnection(String sourceName) {
		ComboPooledDataSource ds=null;
		try{
			synchronized(dataMap){
				ds=(ComboPooledDataSource)dataMap.get(sourceName);
			}
			Connection conn = ds.getConnection();
			if(bResetFlag = true)
				bResetFlag = false;
			return conn;
	    }catch(SQLException e) {
	    	log.error("获取连接出错"+e.getMessage());
	    	if(!bResetFlag) {
	    		log.info("尝试重新建立连接");
	    		ds.hardReset();
	    		bResetFlag = true;
	    	} else {
	    		log.error("数据连接已断开,尝试重新连接无效,系统将退出");
	    		// TODO return error connection
	    	}
	    }
	    return null;
	}
	
	/**
	   * 方法
	   *
	   * 判断是否创建了指定的数据源
	   */
	  public  boolean hasDatabase(String databaseName) {
	    return dataMap.containsKey(databaseName);
	  }
	
	/**java的析构函数
	 *在销毁类之前 关闭连接池 
	 */
	protected void finalize() throws Throwable {
		for(int i=0;i<dataMap.size();i++)
	    DataSources.destroy((ComboPooledDataSource)dataMap.get(i)); //关闭datasource
	    super.finalize();
	}

	public String getURL_PROTOCOL() {
		return URL_PROTOCOL;
	}

	public void setURL_PROTOCOL(String uRLPROTOCOL) {
		URL_PROTOCOL = uRLPROTOCOL;
	}
	public String getCONSOLE_PROPERTIES() {
		return CONSOLE_PROPERTIES;
	}
	public void setCONSOLE_PROPERTIES(String cONSOLEPROPERTIES) {
		CONSOLE_PROPERTIES = cONSOLEPROPERTIES;
	}
	
}
