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

import zima.springboot.model.Comment;
import zima.springboot.payload.CommentRequest;
import zima.springboot.payload.PagedResponse;
import zima.springboot.security.CurrentUser;
import zima.springboot.security.UserPrincipal;
import zima.springboot.service.CommentService;
import zima.springboot.util.AppConstants;

@RestController
@RequestMapping("/campgrounds/{campgroundId}/comments")
public class CommentController {

	private final CommentService commentService;
	
	@Autowired
	private CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@GetMapping
    public PagedResponse<?> getAllComments(
            @PathVariable(name = "campgroundId") Long campgroundId,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
        return commentService.getAllComments(campgroundId, 15, 30);
    }
	
	// Create Campground Form
	@GetMapping("/new")
	public ModelAndView getNewPage(@PathVariable long id) {
		return new ModelAndView("newComment", "comments", new Comment());
	}

	
	// Post Comment
	@PostMapping
	@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest commentRequest, @PathVariable(name = "campgroundId") Long campgroundId, @CurrentUser UserPrincipal currentUser){
        return commentService.addComment(commentRequest, campgroundId, currentUser);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getComment(@PathVariable(name = "campgroundId") Long campgroundId, @PathVariable(name = "id") Long id){
        return commentService.getComment(campgroundId, id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateComment(@PathVariable(name = "campgroundId") Long campgroundId, @PathVariable(name = "id") Long id, @Valid @RequestBody CommentRequest commentRequest, @CurrentUser UserPrincipal currentUser){
        return commentService.updateComment(campgroundId, id, commentRequest, currentUser);
    }
	
	// DELETE - removes comment from the database
	@DeleteMapping("/{id}")
	 @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "campgroundId") Long campgroundId, @PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser){
        return commentService.deleteComment(campgroundId, id, currentUser);
    }
}
