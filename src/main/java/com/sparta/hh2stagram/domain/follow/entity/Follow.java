package com.sparta.hh2stagram.domain.follow.entity;

import com.sparta.hh2stagram.domain.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Long followingUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id")
    private User follower;

    @Builder
    public Follow(Long following, User follower){
        this.followingUserId = following;
        this.follower = follower;
    }

    // follower, following 페이저블 하면서 만들었어요. 커밋이 안되서 우선 올립니다 ㅠㅠ!
    public Object getFollowerId() {
        return follower.getId();
    }
}
