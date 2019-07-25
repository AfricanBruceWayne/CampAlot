package zima.springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import zima.springboot.model.Campground;

@Repository
public interface CampgroundRepository extends PagingAndSortingRepository<Campground, Long> {
	Page<Campground> findByCreatedBy(Long userId, Pageable pageable);
	
	Long countByCreatedBy(Long userId);
}
