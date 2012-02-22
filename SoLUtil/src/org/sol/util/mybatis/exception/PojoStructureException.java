package org.sol.util.mybatis.exception;

public class PojoStructureException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PojoStructureException(String msg) {
		super(msg);
	}
	
	public PojoStructureException(String msg,Throwable e) {
		super(msg,e);
	}
}
