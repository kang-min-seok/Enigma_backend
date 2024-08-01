package com.minseok.enigma.enigma.controller;

import com.minseok.enigma.enigma.dto.request.comment.CommentCreateRequest;
import com.minseok.enigma.enigma.dto.response.comment.CommentResponse;
import com.minseok.enigma.enigma.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 댓글 관련 요청을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성 요청을 처리합니다.
     *
     * @param commentCreateRequest 댓글 작성 요청 DTO
     * @return 작성된 댓글 응답 DTO와 함께 HTTP 상태 코드 201을 반환합니다.
     */
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        CommentResponse response = commentService.createComment(commentCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 게시글에 달린 댓글을 조회합니다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @return 댓글 목록과 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @GetMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId, @PathVariable Long userId) {
        List<CommentResponse> responses = commentService.getCommentsByPost(postId, userId);
        return ResponseEntity.ok(responses);
    }
}
