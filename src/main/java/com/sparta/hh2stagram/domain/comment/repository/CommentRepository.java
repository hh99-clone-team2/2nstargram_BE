package com.sparta.hh2stagram.domain.comment.repository;

import com.sparta.hh2stagram.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
