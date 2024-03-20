package com.sparta.hh2stagram;

import com.sparta.hh2stagram.domain.likes.entity.Likes;
import com.sparta.hh2stagram.domain.likes.repository.LikesRepository;
import com.sparta.hh2stagram.domain.likes.service.LikesService;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.repository.PostRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LikesServiceTest {

    private LikesService likesService;

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        likesRepository = mock(LikesRepository.class);
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);
        likesService = new LikesService(likesRepository, postRepository, userRepository);
    }

    @Test
    void likeOrUnlikePost_LikeExists_SuccessfullyUnliked() {

        UserDetails userDetails = mock(UserDetails.class);

        User user = new User("test@example.com","010-4345-2342","Aabcd123!","sanha","san");
        Post post = new Post(1L);


        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Likes existingLike = new Likes(user, post);
        when(likesRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(existingLike));


        String message = likesService.likeOrUnlikePost(1L, userDetails);


        verify(likesRepository, times(1)).delete(existingLike);


        assertEquals("좋아요가 취소되었습니다.", message);
    }

    @Test
    void likeOrUnlikePost_LikeDoesNotExist_SuccessfullyLiked() {

        UserDetails userDetails = mock(UserDetails.class);

        User user = new User("test@example.com","010-4345-2342","Aabcd123!","sanha","san");
        Post post = new Post(1L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        when(likesRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());

        String message = likesService.likeOrUnlikePost(1L, userDetails);

        verify(likesRepository, times(1)).save(any());


        assertEquals("좋아요가 추가되었습니다.", message);
    }

    @Test
    void likeOrUnlikePost_UserNotFound_ThrowException() {

        UserDetails userDetails = mock(UserDetails.class);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(CustomApiException.class, () -> likesService.likeOrUnlikePost(1L, userDetails));
    }

    @Test
    void likeOrUnlikePost_PostNotFound_ThrowException() {
        UserDetails userDetails = mock(UserDetails.class);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User("test@example.com","010-4345-2342","Aabcd123!","sanha","san")));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomApiException.class, () -> likesService.likeOrUnlikePost(1L, userDetails));
    }
}