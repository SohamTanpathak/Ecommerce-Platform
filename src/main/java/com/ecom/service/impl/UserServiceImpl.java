package com.ecom.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepo;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDtls saveUser(UserDtls user) {
		user.setRole("ROLE_USER");
		user.setIsEnable(true);
		user.setAccountNonLocked(true);
		user.setFailedAttempt(0);

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		UserDtls saveUser = userRepo.save(user);
		return saveUser;
	}

	@Override
	public UserDtls getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public List<UserDtls> getUsers(String role) {
		return userRepo.findByRole(role);
		
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		Optional<UserDtls> findByUser = userRepo.findById(id);
		
		if(findByUser.isPresent()) {
			UserDtls userDtls = findByUser.get();
			userDtls.setIsEnable(status);
			userRepo.save(userDtls);
			return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttempt(UserDtls user) {
		int attempt = user.getFailedAttempt() + 1;
		user.setFailedAttempt(attempt);
		userRepo.save(user);
	}

	@Override
	public void userAccountLocked(UserDtls user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userRepo.save(user);
		
	}

	@Override
	public boolean unlockAccountTimeExpired(UserDtls user) {
		long lockTime = user.getLockTime().getTime();	// if lockTime = 6pm
		long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;	//then unlockTime will be 7pm
		
		long currentTime = System.currentTimeMillis();
		
		if(unlockTime < currentTime) {
			user.setAccountNonLocked(true);
			user.setFailedAttempt(0);
			user.setLockTime(null);
			userRepo.save(user);
			return true;
		}
		
		return false;
	}

	@Override
	public void resetAttempt(int userId) {
		// TODO Auto-generated method stub
		
	}

	
}
