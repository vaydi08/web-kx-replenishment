package com.megajoy.thirdpart.report.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.sol.util.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.thread.ThreadManager;

/**
 * ����̨��¼����
 * @author HUYAO
 *
 */
@Controller
public class SocketServer extends Thread{
	private ServerSocket serverSocket;
	
	@Value("${listenerPort}")
	private int port;
	
	// �̹߳�����
	@Autowired
	private ThreadManager threadManager;
	
	@Override
	public void run() {
		Logger.MONITOR.info("��ʼ���� [" + port + "] �˿ڵ�SOCKET����");
		while(true) {
			try {
				Socket socket = serverSocket.accept();

				Logger.MONITOR.linfo("��ȡ��Զ�̵�¼����.", socket.getInetAddress().getHostName(),socket.getPort());
				Server server = new Server(socket);
				server.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ������¼���Ʒ�����
	 * @return
	 */
	public boolean startServer() {
		Logger.MONITOR.info("����SOCKET�������");
		
		try {
			serverSocket = new ServerSocket(port);
			start();
		} catch (IOException e) {
			return false;
		}
				
		return true;
	}
	
	@Controller
	private class Server extends Thread {
		private Socket socket;
		
		public Server(Socket socket) {
			this.socket = socket;
		}
				
		@Override
		public void run() {
			InputStream is = null;
			OutputStream os = null;
			BufferedReader br = null;
			BufferedWriter bw = null;
			
			try {
				is = socket.getInputStream();
				os = socket.getOutputStream();
				br = new BufferedReader(new InputStreamReader(is));
				bw = new BufferedWriter(new OutputStreamWriter(os));
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
				}
				return;
			}
			
			while(true) {
				try {
					String cmd = br.readLine();
					if(cmd.equalsIgnoreCase("t")) {
						bw.write(threadManager.threadsStatus());
						bw.newLine();
						bw.flush();
					} else if(cmd.equalsIgnoreCase("w")) {
						bw.write(threadManager.queueStatus());
						bw.newLine();
						bw.flush();
					} else if(cmd.equalsIgnoreCase("exit")) {
						bw.write("shut down all thread...");
						bw.newLine();
						bw.flush();
						System.exit(0);
					} else if(cmd.equalsIgnoreCase("quit")) {
						os.close();
						is.close();
						socket.close();
						return;
					}
					
				} catch (IOException e) {
					try {
						os.write(("ERROR:" + e.getMessage()).getBytes());
						os.close();
						is.close();
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
