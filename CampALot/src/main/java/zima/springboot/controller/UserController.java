package zima.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zima.springboot.model.Campground;
import zima.springboot.model.User;
import zima.springboot.payload.PagedResponse;
import zima.springboot.payload.UserIdentityAvailability;
import zima.springboot.payload.UserProfile;
import zima.springboot.payload.UserSummary;
import zima.springboot.security.CurrentUser;
import zima.springboot.security.UserPrincipal;
import zima.springboot.service.CampgroundService;
import zima.springboot.service.UserService;
import zima.springboot.util.AppConstants;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
    private final CampgroundService campgroundService;

    @Autowired
    public UserController(UserService userService, CampgroundService campgroundService) {
        this.userService = userService;
        this.campgroundService = campgroundService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser){
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username){
        return userService.checkUsernameAvailability(username);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email){
        return userService.checkEmailAvailability(email);
    }

    @GetMapping("/{username}/profile")
    public UserProfile getUSerProfile(@PathVariable(value = "username") String username){
        return userService.getUserProfile(username);
    }

    @GetMapping("/{username}/campgrounds")
    public PagedResponse<Campground> getPostsCreatedBy(
            @PathVariable(value = "username") String username,
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
        return campgroundService.getCampgroundsCreatedBy(username, 10, 30);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User newUser, @PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser){
        return userService.updateUser(newUser, username, currentUser);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser){
        return userService.deleteUser(username, currentUser);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> giveAdmin(@PathVariable(name = "username") String username){
        return userService.giveAdmin(username);
    }

    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> takeAdmin(@PathVariable(name = "username") String username){
        return userService.takeAdmin(username);
    }

}
