package org.sol.util.common;

import javax.annotation.PostConstruct;

import org.sol.util.log.Logger;

/**
 * 用于程序关闭的钩子 
 * 继承此类,并实现doFinal()方法
 * @author HUYAO
 *
 */
public class ShutdownHooker {
	protected void doFinal() {};
	
	/**
	 * 调用此方法注册关闭事件钩子
	 */
	@PostConstruct
	public final void registerShutdownHooker() {
		Logger.SYS.init("注册关闭事件钩子. 使用的实现类:%s",getClass());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				doFinal();
			}
		});
	}
}
