package com.sparta.hh2stagram.domain.likes.service;

import com.sparta.hh2stagram.domain.likes.entity.Likes;
import com.sparta.hh2stagram.domain.likes.repository.LikesRepository;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.repository.PostRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j(topic = "좋아요 서비스")
@Service
public class LikesService {

    /*
     * 게시물 좋아요 기능 구현
     * likesRepository : 좋아요 데이터 관리
     * postRepository : 게시물 데이터 확인 */

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikesService(LikesRepository likesRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likesRepository = likesRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String likeOrUnlikePost(Long postId, Long userId) {
        /*유저 정보, 게시물 정보 확인 후 좋아요 정보 저장
         * 에러 발생 시 rollback*/
        /*사용자 검증*/
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다.: " + userId));

        /*게시물 검증*/
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException("게시물을 찾을 수 없습니다.: " + postId));

        /* 좋아요 추가,삭제*/
        Optional<Likes> existingLike = likesRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            /*좋아요가 이미 있으면 삭제*/
            likesRepository.delete(existingLike.get());
            return "좋아요가 취소되었습니다.";
        } else {
            /*좋아요가 없으면 추가*/
            Likes newLike = Likes.builder()
                    .user(user)
                    .post(post)
                    .build();
            likesRepository.save(newLike);
            return "좋아요가 추가되었습니다.";
        }
    }
}