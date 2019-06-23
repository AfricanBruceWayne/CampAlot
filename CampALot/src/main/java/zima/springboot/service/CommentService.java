package zima.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zima.springboot.exception.ResourceNotFoundException;
import zima.springboot.model.Comment;
import zima.springboot.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	public List<Comment> getAllComments(){
		return commentRepository.findAll();
	}
	
	public Comment createComment(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public Map<String, Boolean> deleteComment(long id) throws ResourceNotFoundException {
		Comment comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found for this id :: " + id));
		commentRepository.delete(comment);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Comment deleted", Boolean.TRUE);
		return response;
	}

}
