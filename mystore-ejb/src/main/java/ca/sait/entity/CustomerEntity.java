package ca.sait.entity;
import java.io.Serializable;
import javax.persistence.*;

import java.util.Arrays;
import java.util.Date;


/**
 * The persistent class for the CUSTOMER_INFO database table implements Customer interface,
 * Serializable, overrides hashCode and equals. Has no-parameter constructor and getters and setters for fields.
 */
@Entity
@Table(name="CUSTOMER_INFO")
@NamedQuery(name= CustomerEntity.findAllCust, query="SELECT c FROM CustomerEntity c")
public class CustomerEntity implements Serializable, ca.sait.model.Customer {
	
	private static final long serialVersionUID = 5321465659919231139L;

	//named query to select all the Customers from the CUSTOMER_INFO table and populate the map
	//of the singleton CustomCacheBean EJB
	public static final String findAllCust = "select_all_cust";

	@Id
	@Column(name="UUID", unique=true, nullable=false)
	private byte [] uuid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name = "EMAIL")
	private String email;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_MODIFIED")
	private Date lastModified;

	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="ROLE_NAME")
	private String roleName;

	@Column(name="USERNAME")
	private String username;

	public CustomerEntity() {
	}
	
	public byte [] getUuid() {
		return this.uuid;
	}

	public void setUuid(byte [] uuid) {
		this.uuid = uuid;
	}

	public Date getCreateDate() {
		return this.createDate;
	}


	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Date LastModified() {
		// TODO Auto-generated method stub
		return this.lastModified;
	}

	@Override
	public void setCreateDate(Date createDate) {
		// TODO Auto-generated method stub
		this.createDate = createDate;
		
	}

	@Override
	public void setLastModified(Date lastModified) {
		// TODO Auto-generated method stub
		this.lastModified = lastModified;
		
	}

	@Override
	public void setUuid(String uuid) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + Arrays.hashCode(uuid);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerEntity other = (CustomerEntity) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (!Arrays.equals(uuid, other.uuid))
			return false;
		return true;
	}

}