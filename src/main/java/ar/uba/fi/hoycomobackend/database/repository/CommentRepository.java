package ar.uba.fi.hoycomobackend.database.repository;

import ar.uba.fi.hoycomobackend.database.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
