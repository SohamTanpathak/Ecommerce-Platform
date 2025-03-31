package com.ecom.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepo;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String email = request.getParameter("username");
		UserDtls userDtls = userRepo.findByEmail(email);
		
		if(userDtls.getIsEnable()) {
			
			if(userDtls.getAccountNonLocked()) {
				
				if(userDtls.getFailedAttempt() < AppConstant.NUMBER_OF_ATTEMPTS) {
					userService.increaseFailedAttempt(userDtls);
				}
				else {
					userService.userAccountLocked(userDtls);
					exception = new LockedException("Your account is locked, as you made 3 failed attempts.");
				}
			}
			else {
				
				if(userService.unlockAccountTimeExpired(userDtls)) {
					exception = new LockedException("Your account is now unlocked. Please try to login.");
				}
				else {
					exception = new LockedException("Your account is locked. Please try after sometime.");
				}	
			}
			
		}
		else {
			exception = new LockedException("Your account is inactive");
		}
		
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}
