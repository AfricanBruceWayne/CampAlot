package zima.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import zima.springboot.exception.ResourceNotFoundException;
import zima.springboot.model.Comment;
import zima.springboot.service.CommentService;

@RestController
@RequestMapping("/campgrounds/{id}/comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	// Create Campground Form
	@GetMapping("/new")
	public ModelAndView getNewPage(@PathVariable long id) {
		return new ModelAndView("newComment", "comments", new Comment());
	}

	
	// Post Comment
	@PostMapping
	public String addComment(@Valid Comment comments, @PathVariable long id, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "newComment?createError";
		}
		commentService.createComment(comments);
		return "redirect:/camgrounds/{id}?createSuccess";
	}
	
	// DELETE - removes comment from the database
	@DeleteMapping("/{id}")
	public String deleteCampground(@PathVariable long id) throws ResourceNotFoundException {
		commentService.deleteComment(id);
		return "redirect:/campgrounds/{id}?deleteSuccess";
	}
}
