package edu.cuit.hzhspace.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户
 * @author Zhonghua Hu
 *
 */
@Entity
@Table(name = "t_user")
public class User extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static final String USER_TYPE_OWNER = "owner";
	public static final String USER_TYPE_GUEST = "guest";

	private String name;
	private String mailAddress;
	private String password;
	private String userType;

	public String getName() {
		return "<font color=\"red\">" + name + "</font>";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
