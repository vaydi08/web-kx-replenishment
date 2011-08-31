package com.sol.kx.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Documented
public @interface Pojo {
	public int index();
	public boolean inherit();
	
}
