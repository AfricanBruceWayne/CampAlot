package zima.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zima.springboot.model.Campground;

public interface CampgroundRepository extends JpaRepository<Campground, Long> {

}
