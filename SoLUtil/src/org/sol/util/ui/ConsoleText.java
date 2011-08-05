package org.sol.util.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * 用于捕获其他输出流,转向到SWT Text控件
 * @author HUYAO
 *
 */
public class ConsoleText{

	private Text text;
	
	private int lineLimit;
	
	/**
	 * 
	 * @param comp 父控件
	 * @param lineLimit 显示行的限制
	 */
	public ConsoleText(Composite comp, int lineLimit) {
		text = new Text(comp, SWT.WRAP|SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
		
		this.lineLimit = lineLimit - 1;
	}
	
	/**
	 * 绑定一个输出流,不重输出到原流
	 * @param os 输出流
	 * @return
	 */
	public PrintStream bindOutputStream(OutputStream os) {
		return new UIPrintStream(os);
	}
	
	/**
	 * 绑定一个输出流,并设置是否输出到原流
	 * @param os 输出流
	 * @param needSendback 是否需要输出到原流
	 * @return
	 */
	public PrintStream bindOutputStream(OutputStream os,boolean needSendback) {
		return new UIPrintStream(os,needSendback);
	}

	/**
	 * 添加Text的文本
	 * @param msg
	 */
	public void appendText(String msg) {
		if(text.getLineCount() > lineLimit) {
			text.setText("");
		}
		
		text.append(msg);
	}
	
	/**
	 * 用于包装的重定向流
	 * @author HUYAO
	 *
	 */
	private class UIPrintStream extends PrintStream {
	
		private OutputStream os;
				
		private boolean needSendback;
		
		public UIPrintStream(OutputStream os) {
			this(os,false);
		}
		
		public UIPrintStream(OutputStream os,boolean needSendback) {
			super(os);
			
			this.os = os;
			this.needSendback = needSendback;
		}
		
		@Override
		public void write(byte[] buf, int off, int len) {
			final String msg = new String(buf,off,len);
					
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					appendText(msg);
//					append("\n");
				}
			});

			if(needSendback) {
				try {
					os.write(buf, off, len);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}