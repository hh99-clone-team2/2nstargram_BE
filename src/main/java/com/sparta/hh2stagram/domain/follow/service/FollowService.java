package com.sparta.hh2stagram.domain.follow.service;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.follow.repository.FollowRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j(topic = "팔로우 서비스")
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String follow(User fromUser, User toUser){
        // 자기 자신 follow 안됨
        if (fromUser == toUser)
            throw new CustomApiException( "자기 자신은 follow 할 수 없습니다.");
        // 중복 follow x
        if (followRepository.findFollow(fromUser, toUser).isPresent())
            throw new CustomApiException("이미 follow 되어있습니다.");
        Follow follow = Follow.builder()
                .follower(fromUser)
                .following(toUser)
                .build();
        followRepository.save(follow);
        return "Success";
    }
}

//    @Transactional
//    public String followOrUnfollowUser(Long followerId, Long followingId, UserDetails userDetails) {
//
//        /*사용자 검증*/
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));
//        // Find the follower and followee users
//        User follower = userRepository.findById(followerId)
//                .orElseThrow(() -> new CustomApiException("팔로워를 찾을 수 없습니다.: " + followerId));
//
//        User following = userRepository.findById(followingId)
//                .orElseThrow(() -> new CustomApiException("팔로우할 사용자를 찾을 수 없습니다.: " + followingId));
//
//        // Check if the follow relationship already exists
//        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(follower, following);
//
//        if (existingFollow.isPresent()) {
//            // If the follow relationship exists, delete it (unfollow)
//            followRepository.delete(existingFollow.get());
//            return "언팔로우 되었습니다.";
//        } else {
//            // If the follow relationship does not exist, create it (follow)
//            Follow newFollow = Follow.builder()
//                    .follower(follower)
//                    .following(following)
//                    .build();
//            followRepository.save(newFollow);
//            return "팔로우 되었습니다.";
//        }
//    }
//}
