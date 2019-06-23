package zima.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zima.springboot.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
