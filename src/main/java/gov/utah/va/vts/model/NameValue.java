package gov.utah.va.vts.model;

import java.io.Serializable;

/**
 * Name/value bean.
 * 
 * @author hnguyen
 *
 */
public class NameValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer active;
	
	private String name;
	
	private String value;
	
	private String op = "insert";
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
}
