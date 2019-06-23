package zima.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import zima.springboot.exception.ResourceNotFoundException;
import zima.springboot.model.Campground;
import zima.springboot.service.CampgroundService;
import zima.springboot.service.CommentService;

@RestController
@RequestMapping("/campgrounds")
public class CampgroundController {

	@Autowired
	private CampgroundService campgroundService;
	
	@Autowired
	private CommentService commentService;
	
	// Index - Get All Campgrounds
	@GetMapping
	public ModelAndView getIndexPage() {
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("campgrounds", campgroundService.getAllCampgrounds());
		return mav;
	}
	
	// Create Campground Form
	@GetMapping("/new")
	public ModelAndView getNewPage() {
		return new ModelAndView("newCampground", "campgrounds", new Campground());
	}
	
	// Show - Get A Campground
	@GetMapping("/{id}")
	public ModelAndView showCampground(@PathVariable long id) throws ResourceNotFoundException {
		ModelAndView mav = new ModelAndView("show");
		mav.addObject("campground", campgroundService.getCampgroundById(id));
		mav.addObject("comments", commentService.getAllComments());
		return mav;
	}
	
	@PostMapping
	public String addCampground(@Valid Campground campgrounds, BindingResult result, 
			Model model) {
		if (result.hasErrors()) {
			return "newCampground?createError";
		}
		campgroundService.createCampground(campgrounds);
		return "redirect:/campgrounds?createSuccess";
	}
	
	// EDIT - shows edit form for a campground
	@GetMapping("/{id}/update")
	public String editCampgroud() {
		return "edit";
	}
	
	// PUT - updates campground in the database
	@PatchMapping("/{id}/edit")
	public String updateACampground(@PathVariable long id, Campground campgrounds) throws ResourceNotFoundException {
		campgroundService.updateCampground(id, campgrounds);
		return "redirect:/campgrounds?updateSuccess";
	}
	
	// DELETE - removes campground and its comments from the database
	@DeleteMapping("/{id}")
	public String deleteCampground(@PathVariable long id) throws ResourceNotFoundException {
		campgroundService.deleteCamgpround(id);
		return "redirect:/campgrounds?deleteSuccess";
	}
	
}
