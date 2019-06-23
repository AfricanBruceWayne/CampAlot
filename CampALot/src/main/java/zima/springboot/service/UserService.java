package zima.springboot.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import zima.springboot.dto.UserRegistrationDto;
import zima.springboot.model.User;

public interface UserService extends UserDetailsService {
	
	User findByUsername(String username);
	
	User registerNewUserAccount(UserRegistrationDto registration);

}
