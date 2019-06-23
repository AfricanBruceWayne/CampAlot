package zima.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import zima.springboot.exception.ResourceNotFoundException;
import zima.springboot.model.Campground;
import zima.springboot.repository.CampgroundRepository;

@Service
public class CampgroundService {

	@Autowired
	private CampgroundRepository campgroundRepository;
	
	public List<Campground> getAllCampgrounds(){
		return campgroundRepository.findAll();
	}
	
	public ResponseEntity<Campground> getCampgroundById(long id) throws ResourceNotFoundException {
		Campground campground =  campgroundRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Campground not found for this id :: " + id));
		return ResponseEntity.ok().body(campground);
	}
	
	public Campground createCampground(Campground campground) {
		return campgroundRepository.save(campground);
	}
	
    public ResponseEntity<Campground> updateCampground(long id,
         Campground campgroundDetails) throws ResourceNotFoundException {
        Campground campground = campgroundRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Campground not found for this id :: " + id));

        campground.setName(campgroundDetails.getName());
        campground.setImage(campgroundDetails.getImage());
        campground.setDescription(campgroundDetails.getDescription());
        final Campground updatedCampground = campgroundRepository.save(campground);
        return ResponseEntity.ok(updatedCampground);
    }
    
    public Map<String, Boolean> deleteCamgpround(long id)
            throws ResourceNotFoundException {
           Campground campground = campgroundRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Campground not found for this id :: " + id));

           campgroundRepository.delete(campground);
           Map<String, Boolean> response = new HashMap<>();
           response.put("Campground deleted", Boolean.TRUE);
           return response;
       }
	
}
