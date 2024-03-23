package com.sparta.hh2stagram.domain.post.repository;

import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Object> findByIdAndUser(Long postId, User user);
    List<Post> findByUser(User user);
    Slice<Post> findByUserIdInOrderByCreatedAtDesc(List<Long> userIdList, Pageable pageable);
}
