package com.sparta.hh2stagram.domain.likes.repository;

import com.sparta.hh2stagram.domain.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository <Likes, Long> {
}
