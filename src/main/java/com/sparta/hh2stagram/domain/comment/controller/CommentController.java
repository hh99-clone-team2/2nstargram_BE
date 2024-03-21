package com.sparta.hh2stagram.domain.comment.controller;

import com.sparta.hh2stagram.domain.comment.dto.CommentRequestDto;
import com.sparta.hh2stagram.domain.comment.dto.CommentResponseDto;
import com.sparta.hh2stagram.domain.comment.service.CommentService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Slf4j(topic = "댓글 작성, 조회, 수정, 삭제")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comment")
    @Operation(summary = "댓글 작성", description = "작성자와 댓글 내용을 등록할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 완료")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.addComment(postId, commentRequestDto, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}/comment")
    @Operation(summary = "댓글 조회", description = "댓글 조회를 할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 완료")
    public ResponseEntity<List<CommentResponseDto>> getComment(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<CommentResponseDto> responseDtoList = commentService.getComment(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PatchMapping("/{postId}/comment/{commentId}")
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 완료")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.updateComment(postId, commentId, commentRequestDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 삭제되었습니다.")
    public ResponseEntity<String> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            commentService.deleteComment(postId, commentId, userDetails);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (CustomApiException ex) {
            throw ex;
        } catch (Exception ex) {
            /* 삭제 중에 예기치 않은 예외가 발생하면 처리하고 500 내부 서버 오류 상태 코드를 반환 */
            throw new CustomApiException( "댓글 삭제에 실패했습니다");
        }
    }
}
