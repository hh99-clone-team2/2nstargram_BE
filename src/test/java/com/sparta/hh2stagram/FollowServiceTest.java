package com.sparta.hh2stagram;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.follow.repository.FollowRepository;
import com.sparta.hh2stagram.domain.follow.service.FollowService;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void follow_WhenValidInputs_Success() {
        User fromUser = mock(User.class);
        User toUser = mock(User.class);

        // Mocking the behavior of followRepository
        when(followRepository.findFollow(fromUser, toUser.getId())).thenReturn(Optional.empty());

        // Call the method under test
        String result = followService.follow(fromUser, toUser);

        // Verify that the followRepository's save method is called once
        verify(followRepository, times(1)).save(any(Follow.class));

        // Assert the result
        assert result.equals("Success");
    }

    @Test
    void follow_WhenSameUser_ThrowsException() {
        User user = mock(User.class);

        // Call the method under test and assert that it throws CustomApiException
        assertThrows(CustomApiException.class, () -> followService.follow(user, user));
    }

    @Test
    void follow_WhenAlreadyFollowed_ThrowsException() {
        User fromUser = mock(User.class);
        User toUser = mock(User.class);

        Follow follow = mock(Follow.class); // Mocking the Follow object

        // Mocking the behavior of followRepository
        when(followRepository.findFollow(fromUser, toUser.getId())).thenReturn(Optional.of(follow));

        // Call the method under test and assert that it throws CustomApiException
        assertThrows(CustomApiException.class, () -> followService.follow(fromUser, toUser));
    }
//    @Test
//    void unfollow_WhenValidInputs_Success() {
//        User fromUser = mock(User.class);
//        User toUser = mock(User.class);
//
//        Follow follow = mock(Follow.class); // Mocking the Follow object
//
//        // Mocking the behavior of followRepository
//        when(followRepository.findFollow(fromUser, toUser)).thenReturn(Optional.of(follow));
//
//        // Call the method under test
//        String result = followService.unfollow(fromUser, toUser);
//
//        // Verify that the followRepository's delete method is called once
//        verify(followRepository, times(1)).delete(follow);
//
//        // Assert the result
//        assertEquals("언팔로우 되었습니다.", result);
//    }
//
//    @Test
//    void unfollow_WhenSameUser_ThrowsException() {
//        User user = mock(User.class);
//
//        // Call the method under test and assert that it throws CustomApiException
//        assertThrows(CustomApiException.class, () -> followService.unfollow(user, user));
//    }
//
//    @Test
//    void unfollow_WhenNotFollowed_ThrowsException() {
//        User fromUser = mock(User.class);
//        User toUser = mock(User.class);
//
//        // Mocking the behavior of followRepository
//        when(followRepository.findFollow(fromUser, toUser)).thenReturn(Optional.empty());
//
//        // Call the method under test and assert that it throws CustomApiException
//        assertThrows(CustomApiException.class, () -> followService.unfollow(fromUser, toUser));
//    }
//
//    @Test
//    void getFollowingList_WhenValidUser_Success() {
//        User user = mock(User.class);
//        List<Follow> followingList = mock(List.class); // Mocking the List of Follow objects
//
//        // Mocking the behavior of followRepository
//        when(followRepository.findByFollower(user)).thenReturn(followingList);
//
//        // Call the method under test
//        List<Follow> result = followService.getFollowingList(userId);
//
//        // Assert the result
//        assertSame(followingList, result);
//    }
//
//    @Test
//    void getFollowerList_WhenValidUser_Success() {
//        User user = mock(User.class);
//        List<Follow> followerList = mock(List.class); // Mocking the List of Follow objects
//
//        // Mocking the behavior of followRepository
//        when(followRepository.findByFollowing(user)).thenReturn(followerList);
//
//        // Call the method under test
//        List<Follow> result = followService.getFollowerList(user);
//
//        // Assert the result
//        assertSame(followerList, result);
//    }
}