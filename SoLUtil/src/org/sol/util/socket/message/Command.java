package org.sol.util.socket.message;

import java.util.HashMap;
import java.util.Map;

public enum Command {
	ACTIVE_TEST(0x70000001),
	EXIT(0x70000002),
	MONITOR_THREAD_REPORT(0x70000003),
	RETURN_STATUS(0x80000001),
	
	STATUS_OK(1),
	STATUS_ERR(-1);
	
	public int CMD;
	
	private Command(int cmd) {
		this.CMD = cmd;
	}
	
	public static class MessageFactory {
		private static final Map<Integer,Class<?>> messageType;
		static {
			messageType = new HashMap<Integer,Class<?>>();
			messageType.put(ACTIVE_TEST.CMD, ActiveTest.class);
			messageType.put(MONITOR_THREAD_REPORT.CMD, MonitorThreadReport.class);
			messageType.put(RETURN_STATUS.CMD, ReturnStatus.class);
		}
		
		public static Content getContent(Header header,byte[] data) {
			if(header.getCommand() == 0)
				return null;
			
			Class<?> contentClass = messageType.get(header.getCommand());
			Content content = null;
			try {
				content = (Content)contentClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			content.setHeader(header);
			
			return content;
		}
	}
}
