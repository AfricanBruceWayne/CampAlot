package zima.springboot.service;

import java.util.Collections;

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
import zima.springboot.model.RoleName;
import zima.springboot.model.User;
import zima.springboot.payload.ApiResponse;
import zima.springboot.payload.PagedResponse;
import zima.springboot.repository.CampgroundRepository;
import zima.springboot.repository.UserRepository;
import zima.springboot.security.UserPrincipal;
import zima.springboot.util.AppConstants;

@Service
public class CampgroundService {

	private final CampgroundRepository campgroundRepository;
	
	private final UserRepository userRepository;
	
	
	@Autowired
	private CampgroundService(CampgroundRepository campgroundRepository, UserRepository userRepository) {
		this.campgroundRepository = campgroundRepository;
		this.userRepository = userRepository;
	};
	
	
	 public PagedResponse<Campground> getAllCampgrounds(int page, int size){
	        validatePageNumberAndSize(page, size);

	        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

	        Page<Campground> campgrounds = campgroundRepository.findAll(pageable);

	        if (campgrounds.getNumberOfElements() == 0){
	            return new PagedResponse<>(Collections.emptyList(), campgrounds.getNumber(), campgrounds.getSize(), campgrounds.getTotalElements(), campgrounds.getTotalPages(), campgrounds.isLast());
	        }

	        return new PagedResponse<>(campgrounds.getContent(), campgrounds.getNumber(), campgrounds.getSize(), campgrounds.getTotalElements(), campgrounds.getTotalPages(), campgrounds.isLast());
	    }

	    public PagedResponse<Campground> getCampgroundsCreatedBy(String username, int page, int size){
	        validatePageNumberAndSize(page, size);
	        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
	        Page<Campground> campgrounds = campgroundRepository.findByCreatedBy(user.getId(), pageable);

	        if(campgrounds.getNumberOfElements() == 0){
	            return new PagedResponse<>(Collections.emptyList(), campgrounds.getNumber(), campgrounds.getSize(), campgrounds.getTotalElements(), campgrounds.getTotalPages(), campgrounds.isLast());
	        }
	        return new PagedResponse<>(campgrounds.getContent(), campgrounds.getNumber(), campgrounds.getSize(), campgrounds.getTotalElements(), campgrounds.getTotalPages(), campgrounds.isLast());
	    }

	    public ResponseEntity<?> updateCampground(Long id, Campground newCampground, UserPrincipal currentUser){
	        Campground campground = campgroundRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", id));
	        if (campground.getUser().getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
	        	campground.setName(newCampground.getName());
	        	campground.setImage(newCampground.getImage());
	        	campground.setDescription(newCampground.getDescription());
	        	Campground updatedCampground = campgroundRepository.save(campground);
	            return new ResponseEntity<>(updatedCampground, HttpStatus.OK);
	        }
	        return new ResponseEntity<>(new ApiResponse(false, "You don't have permission to edit this campground"), HttpStatus.UNAUTHORIZED);
	    }

	    public ResponseEntity<?> deleteCampground(Long id, UserPrincipal currentUser){
	        Campground campground = campgroundRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", id));
	        if (campground.getUser().getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))){
	        	campgroundRepository.deleteById(id);
	            return new ResponseEntity<>(new ApiResponse(true, "You successfully deleted campground"), HttpStatus.OK);
	        }
	        return new ResponseEntity<>(new ApiResponse(true, "You don't have permission to delete this campground"), HttpStatus.UNAUTHORIZED);
	    }

	    public ResponseEntity<?> addCampground(Campground campground, UserPrincipal currentUser){
	        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", 1L));
	        campground.setUser(user);
	        Campground newCampground =  campgroundRepository.save(campground);
	        return new ResponseEntity<>(newCampground, HttpStatus.CREATED);
	    }

	    public ResponseEntity<?> getCampground(Long id){
	        Campground campground = campgroundRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Campground", "id", id));
	        return new ResponseEntity<>(campground, HttpStatus.OK);
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
