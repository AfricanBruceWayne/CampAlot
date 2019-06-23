package zima.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import zima.springboot.dto.UserRegistrationDto;
import zima.springboot.model.User;
import zima.springboot.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}
	
	@GetMapping("/signup")
	public String showRegisgrationForm(WebRequest request, Model model) {
		UserRegistrationDto userDto = new UserRegistrationDto();
		model.addAttribute("user", userDto);
		return "register";
	}
	
	
//	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
//			BindingResult result) {
//		
//		User existingUser = userService.findByUsername(userDto.getUsername());
//		if (existingUser != null) {
//			result.rejectValue("username", null, "There is already an account registered with that username");
//		}
//		
//		if (result.hasErrors()) {
//			return "register?loginFailure";
//		}
//		
//		userService.save(userDto);
//		return "redirect:/campgrounds?registerSuccess";
//		
//	}
	@PostMapping("/register")
	public ModelAndView registerUserAccount(
			  @ModelAttribute("user") @Valid UserRegistrationDto accountDto, 
			  BindingResult result, 
			  WebRequest request, 
			  Errors errors) {
			     
			    User registered = new User();
			    if (!result.hasErrors()) {
			        registered = createUserAccount(accountDto, result);
			    }
			    if (registered == null) {
			        result.rejectValue("username", "register?registerFailure");
			    }
			    if (result.hasErrors()) {
			        return new ModelAndView("register?registerFailure", "user", accountDto);
			    } 
			    else {
			        return new ModelAndView("redirect:/campgrounds?registerSuccess", "user", accountDto);
			    }
			}
	
	private User createUserAccount(UserRegistrationDto accountDto, BindingResult result) {
		User registered = null;
		try {
			registered = userService.registerNewUserAccount(accountDto);
		} catch (UsernameNotFoundException e) {
			return null;
		}
		return registered;
	}

}
