package aiss.videominer.repository;

import aiss.videominer.model.Comment;
import aiss.videominer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findByAuthor(User author, Pageable pageable);
}
