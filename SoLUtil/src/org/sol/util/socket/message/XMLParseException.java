package org.sol.util.socket.message;

public class XMLParseException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public XMLParseException(String msg) {
		super(msg);
	}
	
	public XMLParseException(String msg,Throwable e) {
		super(msg,e);
	}
}
