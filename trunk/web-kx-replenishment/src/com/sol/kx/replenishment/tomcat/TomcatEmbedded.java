package com.sol.kx.replenishment.tomcat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tomcat嵌入式Server，可以创建一个不支持加密的，侦听在本机127.0.0.1上的一个tomcat服务器
 * 
 * 修改一些默认属性,把一些目录结构和tomcat的server.xml对应起来. 加入webapp应用自动获取解压配置等功能
 * 
 * @author Dennis Lv (weer99@163.com) Create By 2008-7-6 下午05:24:43
 */
public class TomcatEmbedded {
	private static final Log logger = LogFactory
			.getLog(TomcatEmbedded.class);

	/**
	 * tomcat启动端口 <code>port</code>
	 */
	private int port = 8898;

	/**
	 * 设置tomcat的主目录 <code>catalinaHome</code>
	 */
	private String catalinaHome = System.getProperty("user.dir") + "/";

	/**
	 * tomcat的engineName <code>engineName</code>
	 */
	private String engineName = "emeddedTomcatServer";

	/**
	 * 设置应用APP路径,对应通常的tomcat/webapps,appbase必须要球在catalinaHome下面，前面不需要/
	 * <code>appBase</code>
	 */
	private String appBase = "webapps/";

	/**
	 * 设置webapp的对应web路径，如果是根路径则为空字符串 <code>docPath</code>
	 */
	private String docPath = "";

	/**
	 * 设置webapp的docbase，这里docbase要求必须在appbase下，前面没有/ <code>docBase</code>
	 */
	private String docBase = "ROOT";

	private boolean started = false;

	private Tomcat tomcat = new Tomcat();

	public boolean isStarted() {
		return started;
	}

	public static void main(String[] args) {
		final TomcatEmbedded t = new TomcatEmbedded();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					t.startup();
				} catch (LifecycleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		
		}).start();

//		System.out.println("ok");
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					while(!t.started) {
//						Thread.sleep(1000);
//					}
//					t.shutdown();
//					System.out.println("end");
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (LifecycleException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
	
	public void startup() throws LifecycleException, ServletException {
		if (!started) {
			init();
			tomcat.start();
			started = true;
			logger.info("TOMCAT内嵌式服务器已启动");
			tomcat.getServer().await();
		}
	}

	public void shutdown() throws LifecycleException {
		if (started) {
			tomcat.stop();
			started = false;
		}
	}

	private void init() throws ServletException {
		tomcat.setBaseDir(catalinaHome);
		tomcat.setPort(port);
		Context context = tomcat.addWebapp("/", catalinaHome + appBase + docBase);

		logger.info("创建TOMCAT服务" + tomcat.getServer().getAddress() + ":" + port);

	}

	/**
	 * 检查并解压war文件
	 * 
	 * @param baseDir
	 */
	private void chkAndExtractWar(File baseDir) {
		FileFilter warFF = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".war");
			}
		};

		// 获取所有的war，判断是否解压，如果没有则进行解压
		File[] warFiles = baseDir.listFiles(warFF);
		for (int i = 0; i < warFiles.length; i++) {
			String warFN = warFiles[i].getName();
			File warDir = new File(baseDir, warFN.substring(0,
					warFN.length() - 4));
			if (!warDir.exists()) {
				try {
					extractWar(warFiles[i], warDir);
				} catch (Exception e) {
					logger.error("解压War文件" + warFN + "到" + warDir + "失败.", e);
				}
			}
		}
	}

	/**
	 * 根据war文件，以及解压到目的目录文件，对war文件进行解压
	 * 
	 * @param warFile
	 * @param targetDir
	 * @throws Exception
	 */
	private static void extractWar(File warFile, File targetDir)
			throws Exception {
		ZipFile zfile = null;
		try {
			zfile = new ZipFile(warFile);
			Enumeration zlist = zfile.entries();
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			while (zlist.hasMoreElements()) {
				// 从ZipFile中得到一个ZipEntry
				ze = (ZipEntry) zlist.nextElement();
				String zeName = ze.getName();
				if (ze.isDirectory()) {
					createDir(targetDir, zeName);
					continue;
				}

				// 写入文件
				OutputStream os = null;
				InputStream is = null;
				try {
					os = new BufferedOutputStream(new FileOutputStream(
							getRealFile(targetDir, zeName)));
					is = new BufferedInputStream(zfile.getInputStream(ze));
					int len = 0;
					while ((len = is.read(buf, 0, buf.length)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							logger.error(e.getMessage(), e);
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			}
		} finally {
			if (zfile != null) {
				try {
					zfile.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据根目录，以及目录路径名称，创建目录结构
	 * 
	 * @param baseDir
	 * @param dirName
	 * @return
	 */
	private static File createDir(File baseDir, String dirName) {
		String[] dirs = dirName.split("/");
		File ret = baseDir;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length; i++) {
				ret = new File(ret, dirs[i]);
			}
		}
		if (!ret.exists()) {
			ret.mkdirs();
		}
		return ret;
	}

	/**
	 * 根据相对目录路径，以及根文件目录，创建实际的文件jsp
	 * 
	 * @param baseDir
	 * @param absFileName
	 * @return
	 */
	private static File getRealFile(File baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = baseDir;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				ret = new File(ret, dirs[i]);
			}
		}
		if (!ret.exists()) {
			ret.mkdirs();
		}
		ret = new File(ret, dirs[dirs.length - 1]);
		return ret;
	}

	/**
	 * 默认装载方式
	 * 
	 * @param host
	 * @param baseDir
	 */
	private void defaultDeploy(Host host, File baseDir) {
		FileFilter appDirFF = new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					// 目录下必须有WEB-INF和WEB-INF目录下的web.xml
					File webInf = new File(pathname, "WEB-INF");
					return webInf.exists()
							&& new File(webInf, "web.xml").exists();
				}
				return false;
			}
		};

		// 获取所有目录，并转成Context在内嵌tomcat中加载
		File[] appDirs = baseDir.listFiles(appDirFF);
		for (int i = 0; i < appDirs.length; i++) {
			String appDirName = appDirs[i].getName();
			String appPath = "/" + appDirName;
			if ("ROOT".equalsIgnoreCase(appDirName)) {
				appPath = "";
			}

			Context ctxRoot = tomcat.addContext(appPath, appDirName);
			ctxRoot.setPrivileged(true);
			ctxRoot.setReloadable(true);
			ctxRoot.addParameter("debug", "0");
			host.addChild(ctxRoot);
			if (logger.isInfoEnabled()) {
				logger.info("appPath:" + appPath);
				logger.info("装载了Web应用" + appDirName);
			}
		}
	}

	/**
	 * 按指定列表的方式加载
	 * 
	 * @param host
	 * @param baseDir
	 */
	private void lstDeploy(Host host, File baseDir) {
		File lstFile = new File(baseDir, "webapps.lst");
		if (!lstFile.exists()) {
			logger.info("使用了指定方式加载Web模块,但" + lstFile.getPath() + "不存在.");
			return;
		}
		FileInputStream fis = null;
		try {
			Properties prop = new Properties();
			fis = new FileInputStream(lstFile);
			prop.load(fis);
			Set entrySet = prop.entrySet();
			for (Iterator iter = entrySet.iterator(); iter.hasNext();) {
				Entry e = (Entry) iter.next();
				String key = (String) e.getKey();
				String val = (String) e.getValue();
				if (val == null || val.trim().length() <= 0) {
					if (logger.isDebugEnabled()) {
						logger.info("应用[" + key + "]所指定的文件路径为空.");
					}
					continue;
				} else {
					Context ctxRoot = tomcat.addContext(key, val);
					ctxRoot.setPrivileged(true);
					ctxRoot.setReloadable(true);
					ctxRoot.addParameter("debug", "0");
					host.addChild(ctxRoot);
					if (logger.isInfoEnabled()) {
						logger.info("装载了Web应用" + key + "(" + val + ")");
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(lstFile.getPath() + "不存在.", e);
		} catch (IOException e) {
			logger.error("加载" + lstFile.getPath() + "属性文件出错.", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public String getAppBase() {
		return appBase;
	}

	public void setAppBase(String appBase) {
		this.appBase = appBase;
	}

	public String getDocBase() {
		return docBase;
	}

	public void setDocBase(String docBase) {
		this.docBase = docBase;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public String getCatalinaHome() {
		return catalinaHome;
	}

	public void setCatalinaHome(String catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}