package zima.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import zima.springboot.exception.AppException;
import zima.springboot.exception.ResourceNotFoundException;
import zima.springboot.model.Role;
import zima.springboot.model.RoleName;
import zima.springboot.model.User;
import zima.springboot.payload.ApiResponse;
import zima.springboot.payload.UserIdentityAvailability;
import zima.springboot.payload.UserProfile;
import zima.springboot.payload.UserSummary;
import zima.springboot.repository.CampgroundRepository;
import zima.springboot.repository.RoleRepository;
import zima.springboot.repository.UserRepository;
import zima.springboot.security.UserPrincipal;

@Service
public class UserService {
	
	private final UserRepository userRepository;
    private final CampgroundRepository campgroundRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, CampgroundRepository campgroundRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.campgroundRepository = campgroundRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSummary getCurrentUser(UserPrincipal currentUser){
        return new UserSummary(currentUser.getId(), currentUser.getUsername());
    }

    public UserIdentityAvailability checkUsernameAvailability(String username){
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserIdentityAvailability checkEmailAvailability(String email){
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    public UserProfile getUserProfile(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Long campgroundCount = campgroundRepository.countByCreatedBy(user.getId());

        return new UserProfile(user.getId(), user.getUsername(), user.getCreatedAt(), user.getEmail(), campgroundCount);
    }

    public ResponseEntity<?> addUser(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken"), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(user.getEmail())){
            return new ResponseEntity<>(new ApiResponse(false, "Email is already taken"), HttpStatus.BAD_REQUEST);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result =  userRepository.save(user);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateUser(User newUser, String username, UserPrincipal currentUser){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        if(user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));

            User updatedUser =  userRepository.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        }

        return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to update profile of: " + username), HttpStatus.UNAUTHORIZED);

    }

    public ResponseEntity<?> deleteUser(String username, UserPrincipal currentUser){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
        if(!user.getId().equals(currentUser.getId())){
            return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to delete profile of: " + username), HttpStatus.UNAUTHORIZED);
        }
        userRepository.deleteById(user.getId());

        return new ResponseEntity<>(new ApiResponse(true, "You successfully deleted profile of: " + username), HttpStatus.OK);
    }

    public ResponseEntity<?> giveAdmin(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException("User role not set")));
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(true, "You gave ADMIN role to user: " + username), HttpStatus.OK);
    }

    public ResponseEntity<?> takeAdmin(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(true, "You took ADMIN role from user: " + username), HttpStatus.OK);
    }

    public ResponseEntity<?> setOrUpdateInfo(UserPrincipal currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
        if (user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
        	
            User updatedUser = userRepository.save(user);

            Long campgroundCount = campgroundRepository.countByCreatedBy(updatedUser.getId());


            UserProfile userProfile = new UserProfile(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getCreatedAt(), updatedUser.getEmail(), campgroundCount);
            return new ResponseEntity<>(userProfile, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to update users profile"), HttpStatus.OK);
    }
	
	
}
