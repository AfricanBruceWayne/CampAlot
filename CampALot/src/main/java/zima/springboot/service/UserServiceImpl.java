package zima.springboot.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import zima.springboot.dto.UserRegistrationDto;
import zima.springboot.model.Role;
import zima.springboot.model.User;
import zima.springboot.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder PWDEncoder;

	public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
	
	public User registerNewUserAccount(UserRegistrationDto registration) {
		User user = new User();
		user.setUsername(registration.getUsername());
		user.setPassword(PWDEncoder.encode(registration.getPassword()));
		user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		return userRepository.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}
		boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
		return new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }



}
