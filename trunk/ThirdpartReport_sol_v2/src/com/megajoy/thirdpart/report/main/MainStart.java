package com.megajoy.thirdpart.report.main;

import org.sol.util.common.ShutdownHooker;
import org.sol.util.log.Logger;
import org.sol.util.thread.BaseThread;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.megajoy.thirdpart.report.service.ConfigService;
import com.megajoy.thirdpart.report.thread.NoDelayThread;
import com.megajoy.thirdpart.report.thread.ThreadManager;


public class MainStart {
	public static void main(String[] args) {
		new MainStart().start();
	}
	
	public void start() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
		
		// 建立配置
		ConfigService configService = (ConfigService) ctx.getBean("configService");
		configService.initConfig();
		
		// 建立配置
		ThreadManager threadManager = ctx.getBean(ThreadManager.class);
		threadManager.initService();
		
		threadManager.startAllThreads();
		
		
		SocketServer socketServer = ctx.getBean(SocketServer.class);
		socketServer.startServer();
		
		ShutDownHook hook = new ShutDownHook(ctx, threadManager);
		hook.registerShutdownHooker();
	}
	
	private class ShutDownHook extends ShutdownHooker {
		private ClassPathXmlApplicationContext ctx;
		
		private ThreadManager threadManager;
		
		public ShutDownHook(ClassPathXmlApplicationContext ctx,ThreadManager threadManager) {
			this.ctx = ctx;
			this.threadManager = threadManager;
		}
		
		@Override
		protected void doFinal() {
			ctx.close();
						
			for(BaseThread gbt : threadManager.getDelayThreadSet()) {
				while(!gbt.isover()) {
					Logger.SYS.info("等待普通执行线程关闭");
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
			
			Logger.SYS.info("中断所有wait线程");
			
			for(NoDelayThread ndt : threadManager.getNoDelayThreadSet())
				if(ndt.isStarted()) ndt.stop();
			threadManager.getDelayThreadGroup().interrupt();
			threadManager.getNoDelayThreadGroup().interrupt();

			for(NoDelayThread ndt : threadManager.getNoDelayThreadSet()) {
				while(!ndt.isover()) {
//					System.out.println(ndt.getThreadName() + " " + ndt.getSwich());
					Logger.SYS.info("等待队列线程关闭");
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
			Logger.SYS.info("序列化所有队列");
			threadManager.serializeQueue();
			
			Logger.SYS.info("All Thread is terminate");
		}
	}
}
