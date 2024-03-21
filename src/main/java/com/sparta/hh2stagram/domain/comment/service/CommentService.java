package com.sparta.hh2stagram.domain.comment.service;

import com.sparta.hh2stagram.domain.comment.dto.CommentRequestDto;
import com.sparta.hh2stagram.domain.comment.dto.CommentResponseDto;
import com.sparta.hh2stagram.domain.comment.entity.Comment;
import com.sparta.hh2stagram.domain.comment.repository.CommentRepository;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.repository.PostRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /* 댓글 추가 */
    @Transactional
    public CommentResponseDto addComment(Long postId, CommentRequestDto commentRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException("게시물을 찾을 수 없습니다"));

        Comment comment = new Comment(commentRequestDto.getContent(), user, post);
        comment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .username(user.getUsername())
                .content(comment.getContent())
                .build();
    }

    /* 댓글 조회 */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComment(Long postId, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException("게시물을 찾을 수 없습니다"));

        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentResponseDto> responseDtos = comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .username(comment.getUser().getUsername())
                        .content(comment.getContent())
                        .build())
                .collect(Collectors.toList());

        return responseDtos;
    }

    /* 댓글 수정 */
    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto, UserDetails userDetails) {

        if (userDetails == null) {
            throw new CustomApiException("사용자 정보가 유효하지 않습니다");
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));


        Comment comment = commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new CustomApiException("댓글을 찾을 수 없습니다"));


        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomApiException("이 댓글을 수정할 권한이 없습니다");
        }


        comment.updateContent(commentRequestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .username(user.getUsername())
                .content(updatedComment.getContent())
                .build();
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteComment(Long id,Long commentId, UserDetails userDetails) {
        /* 유저 id로 댓글 찾기 */
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException( "댓글을 찾을 수 없습니다."));


        String username = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다."));


        if (!comment.getUser().equals(currentUser)) {
            throw new CustomApiException("해당 댓글을 삭제할 수 있는 권한이 없습니다.");
        }
        if (!comment.getPost().getId().equals(id)){
            throw new CustomApiException("해당 게시물에 속하지 않은 댓글은 삭제할 수 없습니다.");
        }


        commentRepository.delete(comment);
    }
}
