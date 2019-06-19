package com.bridgelabz.fundoo.user.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Entity
@Table
@Component
public class User implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId")
	private Long userId;
	@NotNull
	@NotEmpty(message = "firstname should not be empty")
	private String firstName;
	@NotNull
	@NotEmpty(message = "lastname should not be empty")
	private String lastName;
	@NotNull
	@NotEmpty(message = "email id should not be empty")
	private String emailId;
	@NotNull
	@NotEmpty(message = "password should not be empty")
	private String password;
	@NotNull
	@NotEmpty(message = "mobile number should not be empty")
	private String mobileNum;
	private boolean isVerify;
	private LocalDateTime registerDate = LocalDateTime.now();
	
	public Long getUserId() 
	{
		return userId;
	}
	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}
	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	public String getLastName() 
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	public String getEmailId() 
	{
		return emailId;
	}
	public void setEmailId(String emailId) 
	{
		this.emailId = emailId;
	}
	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public String getMobileNum() 
	{
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) 
	{
		this.mobileNum = mobileNum;
	}
	public boolean isVerify() 
	{
		return isVerify;
	}
	public void setVerify(boolean isVerify) 
	{
		this.isVerify = isVerify;
	}
	public LocalDateTime getRegisterDate() 
	{
		return registerDate;
	}
	public void setRegisterDate(LocalDateTime registerDate) 
	{
		this.registerDate = registerDate;
	}
	@Override
	public String toString() 
	{
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", password=" + password + ", mobileNum=" + mobileNum + ", isVerify=" + isVerify + ", registerDate="
				+ registerDate + "]";
	}
}
