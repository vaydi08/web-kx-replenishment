package org.sol.util.c3p0.dataEntity2;

public class CriteriaCondition {
	private String name;
	
	private String type;
	
	private Object value1;
	private Object value2;

	public CriteriaCondition(String name,String type,Object value) {
		this.name = name;
		this.type = type;
		this.value1 = value;
	}
	
	public CriteriaCondition(String name,String type,Object value1,Object value2) {
		this.name = name;
		this.type = type;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}
}
