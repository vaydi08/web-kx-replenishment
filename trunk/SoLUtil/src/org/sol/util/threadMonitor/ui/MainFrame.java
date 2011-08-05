package org.sol.util.threadMonitor.ui;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.sol.util.threadMonitor.ThreadInfo;
import org.sol.util.threadMonitor.ThreadMonitorServer;
import org.sol.util.threadMonitor.ThreadMonitorServerListener;
import org.sol.util.ui.ConsoleText;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class MainFrame extends ApplicationWindow {

	// 菜单
	private Action menu_start;
	private Action menu_close;
	private SelectionAdapter btnEvent;
	
	
	// 控件
	private Label lbPort;
	private Text txtPort;
	private Button cbDebug;
	private Button btnStartup;
	
	private TabFolder threadTabFolder;
	private TabItem serverStatus;
	private Map<String,TabItem> threadTabMap;
	
	private ConsoleText console;
	
	
	// 服务器组件
	private ThreadMonitorServer server;
	
	/**
	 * Create the application window.
	 */
	public MainFrame() {
		super(null);
		createActions();
//		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		SashForm sash = new SashForm(parent, SWT.NONE);
		Composite container = new Composite(sash, SWT.NONE);
		container.setLayout(new FormLayout());

		lbPort = new Label(container, SWT.NONE);
		lbPort.setText("端口:");
		lbPort.setLayoutData(new FormData());
		FormData fd_lbPort = new FormData();
		fd_lbPort.top = new FormAttachment(2);
		fd_lbPort.left = new FormAttachment(2);
		lbPort.setLayoutData(fd_lbPort);
		
		txtPort = new Text(container, SWT.BORDER);
		FormData fd_txtPort = new FormData();
		fd_txtPort.top = new FormAttachment(lbPort,-1,SWT.TOP);
		fd_txtPort.left = new FormAttachment(lbPort,5);
		txtPort.setLayoutData(fd_txtPort);
		
		cbDebug = new Button(container, SWT.CHECK);
		cbDebug.setSelection(true);
		cbDebug.setText("DEBUG");
		FormData fd_cbDebug = new FormData();
		fd_cbDebug.top = new FormAttachment(txtPort,1,SWT.TOP);
		fd_cbDebug.left = new FormAttachment(txtPort,5);
		cbDebug.setLayoutData(fd_cbDebug);
		
		btnStartup = new Button(container,SWT.PUSH);
		btnStartup.setText("启动");
		FormData fd_btnStartup = new FormData();
		fd_btnStartup.right = new FormAttachment(cbDebug, 60, SWT.RIGHT);
		fd_btnStartup.top = new FormAttachment(cbDebug,-2,SWT.TOP);
		fd_btnStartup.left = new FormAttachment(cbDebug,5);
		btnStartup.setLayoutData(fd_btnStartup);
		btnStartup.addSelectionListener(btnEvent);
		
		threadTabFolder = new TabFolder(container, SWT.NONE);
		FormData fd_threadTabFolder = new FormData();
		fd_threadTabFolder.top = new FormAttachment(txtPort, 10);
		fd_threadTabFolder.left = new FormAttachment(2);
		fd_threadTabFolder.right = new FormAttachment(100,-10);
		fd_threadTabFolder.bottom = new FormAttachment(100);
		threadTabFolder.setLayoutData(fd_threadTabFolder);
		
		serverStatus = new TabItem(threadTabFolder, SWT.NONE);
		serverStatus.setText("服务器状态");
		
		
		
		console = new ConsoleText(sash,500);
		PrintStream ps = console.bindOutputStream(System.out, true);
		System.setOut(ps);
		System.setErr(ps);

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		menu_start = new Action("启动服务器(&O)@Ctrl+O") {
			@Override
			public void run() {
				startServer();
			}
		};
		menu_close = new Action("终止服务器(&E)@Ctrl+E") {
			@Override
			public void run() {
				closeServer();
			}
		};
		menu_close.setEnabled(false);
		
		btnEvent = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startServer();
			}
		};
	}
	
	/**
	 * 启动服务器
	 */
	private void startServer() {
		int port = Integer.parseInt(txtPort.getText());
		boolean debug = cbDebug.getSelection();
		ServerListener listener = new ServerListener();
		if(server == null) 
			server = new ThreadMonitorServer(port, debug, listener);
		server.startServer();
		
		btnStartup.setEnabled(false);
		menu_start.setEnabled(false);
		
		menu_close.setEnabled(true);
	}
	
	/**
	 * 终止服务器
	 */
	private void closeServer() {
		if(server != null) 
			server.close();
		btnStartup.setEnabled(true);
		menu_start.setEnabled(true);
		
		menu_close.setEnabled(false);
		
		console.appendText("服务器已终止,再次点击关闭按钮将退出");
	}
	
	private class ServerListener implements ThreadMonitorServerListener {
		@Override
		public void onNewThreadRegister(final ThreadInfo info) {
			if(threadTabMap == null)
				threadTabMap = new HashMap<String,TabItem>();

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						TabItem item = threadTabMap.get(info.getThreadGroupname());
						if(item == null) {
							item = new TabItem(threadTabFolder, SWT.NONE);
							item.setText(info.getThreadGroupname());
							
							threadTabMap.put(info.getThreadGroupname(), item);
							
							ThreadInfoTabComp comp = new ThreadInfoTabComp(threadTabFolder, SWT.NONE);
							item.setControl(comp);
							comp.setInput(server.getThreadInfoList(info.getThreadGroupname()));
							
							threadTabFolder.setSelection(item);
						} else {
							ThreadInfoTabComp comp = (ThreadInfoTabComp)item.getControl();
							comp.refresh();
						}
					}
				});
		}

		@Override
		public void onActiveTest(final ThreadInfo info) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					TabItem item = threadTabMap.get(info.getThreadGroupname());
					if(item != null) {
						ThreadInfoTabComp comp = (ThreadInfoTabComp)item.getControl();
						comp.refresh();
					}
				}
			});
		}

		@Override
		public void onDisconnected(final ThreadInfo info) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					TabItem item = threadTabMap.get(info.getThreadGroupname());
					if(item != null) {
						ThreadInfoTabComp comp = (ThreadInfoTabComp)item.getControl();
						comp.refresh();
					}
				}
			});
		}
	}

	@Override
	protected void handleShellCloseEvent() {
		if(menu_close.isEnabled()) {
			closeServer();
		} else {
			super.handleShellCloseEvent();
		}
	}
	
	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		
		MenuManager menuSystem = new MenuManager("系统");
		menuSystem.add(menu_start);
		menuSystem.add(menu_close);
		
		menuManager.add(menuSystem);
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MainFrame window = new MainFrame();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
