package com.sparta.hh2stagram.domain.comment.controller;

import com.sparta.hh2stagram.domain.comment.dto.CommentRequestDto;
import com.sparta.hh2stagram.domain.comment.dto.CommentResponseDto;
import com.sparta.hh2stagram.domain.comment.service.CommentService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import com.sparta.hh2stagram.global.refreshToken.dto.ResponseDto;
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
@RequestMapping("/api/comments")
@Slf4j(topic = "댓글 작성, 조회, 수정, 삭제")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/add")
    @Operation(summary = "댓글 작성", description = "작성자와 댓글 내용을 등록할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 완료")
    public ResponseEntity<?> addComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.addComment(postId, commentRequestDto, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success("댓글이 작성되었습니다.",responseDto));

//        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success("게시물이 공유되었습니다.", responseDto));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 조회", description = "댓글 조회를 할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 완료")
    public ResponseEntity<?> getComment(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<CommentResponseDto> responseDtoList = commentService.getComment(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success("댓글 조회 완료",responseDtoList));
    }

    @PatchMapping("/{postId}/update/{commentId}")
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 완료")
    public ResponseEntity<?> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.updateComment(postId, commentId, commentRequestDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success("댓글 수정 완료",responseDto));
    }

    @DeleteMapping("/{postId}/delete/{commentId}")
    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 삭제되었습니다.")
    public ResponseEntity<?> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            commentService.deleteComment(postId, commentId, userDetails);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success("댓글이 삭제되었습니다.", null));
        } catch (CustomApiException ex) {
            throw ex;
        } catch (Exception ex) {
            /* 삭제 중에 예기치 않은 예외가 발생하면 처리하고 500 내부 서버 오류 상태 코드를 반환 */
            throw new CustomApiException( "댓글 삭제에 실패했습니다");
        }
    }
}
