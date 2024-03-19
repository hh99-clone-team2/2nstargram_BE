package com.sparta.hh2stagram.domain.follow.entity;

import com.sparta.hh2stagram.domain.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Tag(name= "follow Table", description = "팔로우 테이블")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long follower;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
