package com.sparta.hh2stagram.domain.post.repository;

import com.sparta.hh2stagram.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository <PostImage,Long> {
}
