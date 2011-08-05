package org.sol.util.socket.message;

import java.io.IOException;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

/**
 * 接收消息确认包
 * @author HUYAO
 *
 */
public class ReturnStatus extends Content{

	private int status;
	
	public ReturnStatus(int sequence,int status) {
		super(sequence,Command.RETURN_STATUS.CMD,4);
		setStatus(status);
	}
	
	public ReturnStatus() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void getSelfBytes(DataOutputStream ds) throws IOException {
		ds.writeInt(status);
	}

	@Override
	protected void parseSelfBytes(DataInputStream ds) throws IOException {
		status = ds.readInt();
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
