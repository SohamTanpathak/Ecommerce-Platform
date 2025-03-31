package com.ecom.service;

import java.util.List;

import org.apache.catalina.User;

import com.ecom.model.UserDtls;

public interface UserService {

	public UserDtls saveUser(UserDtls user);
	
	public UserDtls getUserByEmail(String email);
	
	public List<UserDtls> getUsers(String role);

	public Boolean updateAccountStatus(Integer id, Boolean status);
	
	public void increaseFailedAttempt(UserDtls user);
	
	public void userAccountLocked(UserDtls user);
	
	public boolean unlockAccountTimeExpired(UserDtls user);
	
	public void resetAttempt(int userId);
	
}
