package zima.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import zima.springboot.exception.BadRequestException;
import zima.springboot.exception.ResourceNotFoundException;
import zima.springboot.model.Campground;
import zima.springboot.model.Comment;
import zima.springboot.model.RoleName;
import zima.springboot.model.User;
import zima.springboot.payload.ApiResponse;
import zima.springboot.payload.CommentRequest;
import zima.springboot.payload.PagedResponse;
import zima.springboot.repository.CampgroundRepository;
import zima.springboot.repository.CommentRepository;
import zima.springboot.repository.UserRepository;
import zima.springboot.security.UserPrincipal;
import zima.springboot.util.AppConstants;

@Service
public class CommentService {
	
	private final CampgroundRepository campgroundRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	
	@Autowired
	public CommentService(CommentRepository commentRepository, CampgroundRepository campgroundRepository, UserRepository userRepository) {
		this.commentRepository = commentRepository;
		this.campgroundRepository = campgroundRepository;
		this.userRepository = userRepository;
	};
	
	public PagedResponse<?> getAllComments(Long campgroundId, int page, int size){
        validatePageNumberAndSize(page, size);
        Campground campground = campgroundRepository.findById(campgroundId).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", campgroundId));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Comment> comments = commentRepository.findByCampgroundId(campgroundId, pageable);

        return new PagedResponse<>(comments.getContent(), comments.getNumber(), comments.getSize(), comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
    }

    public ResponseEntity<?> addComment(CommentRequest commentRequest, Long campgroundId, UserPrincipal currentUser){
        Campground campground = campgroundRepository.findById(campgroundId).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", campgroundId));
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
        Comment comment = new Comment(commentRequest.getContent());
        comment.setUser(user);
        comment.setCampground(campground);
        comment.setName(currentUser.getUsername());
        Comment newComment = commentRepository.save(comment);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getComment(Long campgroundId, Long id){
        Campground campground = campgroundRepository.findById(campgroundId).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", campgroundId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        if (comment.getCampground().getId().equals(campground.getId())){
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "Comment does not belong to post"), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updateComment(Long campgroundId, Long id, CommentRequest commentRequest, UserPrincipal currentUser){
        Campground campground = campgroundRepository.findById(campgroundId).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", campgroundId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        if (!comment.getCampground().getId().equals(campground.getId())){
            return new ResponseEntity<>(new ApiResponse(false, "Comment does not belong to post"), HttpStatus.BAD_REQUEST);
        }

        if (comment.getUser().getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
            comment.setContent(commentRequest.getContent());
            Comment updatedComment = commentRepository.save(comment);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to update this comment"), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> deleteComment(Long campgroundId, Long id, UserPrincipal currentUser){
        Campground campground = campgroundRepository.findById(campgroundId).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", campgroundId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        if (!comment.getCampground().getId().equals(campground.getId())){
            return new ResponseEntity<>(new ApiResponse(false, "Comment does not belong to post"), HttpStatus.BAD_REQUEST);
        }

        if (comment.getUser().getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
            commentRepository.deleteById(comment.getId());
            return new ResponseEntity<>(new ApiResponse(true, "You successfully deleted comment"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to delete this comment"), HttpStatus.BAD_REQUEST);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size < 0) {
            throw new BadRequestException("Size number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
	
	

}
