package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_auth")
public class SysAuth {
	@Id
	private Integer id;
	
	@Column(name = "groupid")
	private Integer groupid;
	private String groupdesc;
	
	@Column(name = "uriid")
	private Integer uriid;
	private String uri;
	private String uriname;
	
	@Column(name = "status")
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getGroupdesc() {
		return groupdesc;
	}

	public void setGroupdesc(String groupdesc) {
		this.groupdesc = groupdesc;
	}

	public Integer getUriid() {
		return uriid;
	}

	public void setUriid(Integer uriid) {
		this.uriid = uriid;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUriname() {
		return uriname;
	}

	public void setUriname(String uriname) {
		this.uriname = uriname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SysAuth ["
				+ (groupdesc != null ? "groupdesc=" + groupdesc + ", " : "")
				+ (groupid != null ? "groupid=" + groupid + ", " : "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (uri != null ? "uri=" + uri + ", " : "")
				+ (uriid != null ? "uriid=" + uriid + ", " : "")
				+ (uriname != null ? "uriname=" + uriname : "") + "]";
	}
}
