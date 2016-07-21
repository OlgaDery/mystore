package ca.sait.model;

import java.util.Date;

//template for the Customer_entity class
public interface Customer {
	
	byte[] getUuid();

	void setUuid(String uuid);

	Date getCreateDate();

	void setCreateDate(Date createDate);

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	String getEmail();

	void setEmail(String email);

	String getFirstName();

	void setFirstName(String firstName);

	Date LastModified();

	void setLastModified(Date lastModified);

	String getLastName();

	void setLastName(String lastName);

	String getModifiedBy();

	void setModifiedBy(String modifiedBy);

	String getPassword();

	void setPassword(String password);

	String getRoleName();

	void setRoleName(String roleName);

	String getUsername();

	void setUsername(String username);

}
