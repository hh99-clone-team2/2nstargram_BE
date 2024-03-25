package com.sparta.hh2stagram.domain.comment.repository;

import com.sparta.hh2stagram.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndId(Long PostId, Long commentId);
    List<Comment> findByPostId(Long postId);
}
