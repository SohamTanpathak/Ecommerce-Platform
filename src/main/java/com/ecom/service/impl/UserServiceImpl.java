package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepo;
import com.ecom.service.UserService;

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

	
}
