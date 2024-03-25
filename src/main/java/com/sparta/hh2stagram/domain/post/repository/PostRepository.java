package com.sparta.hh2stagram.domain.post.repository;

import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Object> findByIdAndUser(Long postId, User user);
    List<Post> findByUser(User user);
    Slice<Post> findByUserIdInOrderByCreatedAtDesc(List<Long> userIdList, Pageable pageable);

    @Query ("SELECT p FROM Post p WHERE p.user.id <= ?1 ORDER BY p.likesCount DESC")
    Slice<Post> findAllUserIdOrderByLikesCountDescAndUser_UsernameAsc(@Param("userId") Long UserId, Pageable pageable);

    Slice<Post> findByUserEqualsOrderByCreatedAtDesc (User user, Pageable pageable);
}
