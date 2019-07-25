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
import org.springframework.web.servlet.ModelAndView;

import zima.springboot.model.Campground;
import zima.springboot.payload.PagedResponse;
import zima.springboot.security.CurrentUser;
import zima.springboot.security.UserPrincipal;
import zima.springboot.service.CampgroundService;
import zima.springboot.util.AppConstants;

@RestController
@RequestMapping("/campgrounds")
public class CampgroundController {

	private final CampgroundService campgroundService;
	
	@Autowired
	private CampgroundController(CampgroundService campgroundService) {
		this.campgroundService = campgroundService;
	}
	
	// Index - Get All Campgrounds
	@GetMapping
	public PagedResponse<Campground> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
    return campgroundService.getAllCampgrounds(10, 30);
}
	
	// Create Campground Form
	@GetMapping("/new")
	public ModelAndView getNewPage() {
		return new ModelAndView("newCampground", "campgrounds", new Campground());
	}
	
	// Show - Get A Campground
	@GetMapping("/{id}")
	public ResponseEntity<?> getPost(@PathVariable(name = "id") Long id){
        return campgroundService.getCampground(id);
    }
	
	@PostMapping
	@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPost(@Valid @RequestBody Campground campground, @CurrentUser UserPrincipal currentUser){
        return campgroundService.addCampground(campground, currentUser);
    }
	
	// EDIT - shows edit form for a campground
	@GetMapping("/{id}/update")
	public String editCampgroud() {
		return "edit";
	}
	
	// PUT - updates campground in the database
	@PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCampground(@PathVariable(name = "id") Long id, @Valid @RequestBody Campground newCampground, @CurrentUser UserPrincipal currentUser){
        return campgroundService.updateCampground(id, newCampground, currentUser);
    }
	
	// DELETE - removes campground and its comments from the database
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCampground(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser){
        return campgroundService.deleteCampground(id, currentUser);
    }
}
