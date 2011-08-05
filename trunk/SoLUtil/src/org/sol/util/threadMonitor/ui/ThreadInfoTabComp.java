package org.sol.util.threadMonitor.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TableColumn;
import org.sol.util.threadMonitor.ThreadInfo;

public class ThreadInfoTabComp extends Composite {
	private Table table;

	private TableViewer tableViewer;
	
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ThreadInfoTabComp(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(100);
		fd_table.bottom = new FormAttachment(100);
		fd_table.top = new FormAttachment(0);
		fd_table.left = new FormAttachment(0);
		table.setLayoutData(fd_table);
		
		TableColumn tcThreadName = new TableColumn(table, SWT.NONE);
		tcThreadName.setWidth(100);
		tcThreadName.setText("线程名");
		
		TableColumn tcFrom = new TableColumn(table, SWT.NONE);
		tcFrom.setWidth(100);
		tcFrom.setText("来源");
		
		TableColumn tcSequence = new TableColumn(table, SWT.NONE);
		tcSequence.setWidth(80);
		tcSequence.setText("包序列号");
		
		TableColumn tcLastActiveTestTimestamp = new TableColumn(table, SWT.NONE);
		tcLastActiveTestTimestamp.setWidth(110);
		tcLastActiveTestTimestamp.setText("上次心跳时间");
		
		TableColumn tcThreadClassname = new TableColumn(table, SWT.NONE);
		tcThreadClassname.setWidth(100);
		tcThreadClassname.setText("线程类");

		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new LabelProvider());

	}

	public void setInput(Object obj) {
		tableViewer.setInput(obj);
	}
	
	public void refresh() {
		tableViewer.refresh();
	}
	
	private class ContentProvider implements IStructuredContentProvider {

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object obj) {
			if(obj instanceof List<?>) 
				return ((List<ThreadInfo>)obj).toArray();
			 else
				return null;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}
		
	}
	
	private class LabelProvider extends org.eclipse.jface.viewers.LabelProvider implements ITableLabelProvider {

		
		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public String getColumnText(Object obj, int col) {
			if(obj instanceof ThreadInfo) {
				ThreadInfo info = (ThreadInfo)obj;
				
				switch (col) {
				case 0:
					return info.getThreadName();
				case 1:
					return info.getAddress() + ":" + info.getPort();
				case 2:
					return Integer.toString(info.getSequence());
				case 3:
					return df.format(info.getLastActiveTestTimestamp());
				case 4:
					return info.getThreadClass();

				default:
					return null;
				}
			} else {
				return null;
			}
		}

	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
