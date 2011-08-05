package org.sol.util.socket.message;

import java.io.IOException;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

/**
 * 心跳包
 * @author HUYAO
 *
 */
public class ActiveTest extends Content{
	
	private int heart;
	
	private static int _heart = Integer.MIN_VALUE;
	
	public ActiveTest(boolean isTest) {
		super(Command.ACTIVE_TEST.CMD, 4);
		setHeart();
	}
	
	public ActiveTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void getSelfBytes(DataOutputStream ds) throws IOException {
		ds.writeInt(heart);
	}
	
	@Override
	protected void parseSelfBytes(DataInputStream ds) throws IOException {
		this.heart = ds.readInt();
	}
	
	public void setHeart() {
		if(_heart == Integer.MAX_VALUE)
			_heart = Integer.MIN_VALUE;
		
		this.heart = ++_heart;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}
}
