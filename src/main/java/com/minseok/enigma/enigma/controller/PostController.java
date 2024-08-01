package com.minseok.enigma.enigma.controller;

import com.minseok.enigma.enigma.dto.request.post.PostCreateRequest;
import com.minseok.enigma.enigma.dto.response.post.PostResponse;
import com.minseok.enigma.enigma.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글 관련 요청을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 작성 요청을 처리합니다.
     *
     * @param postCreateRequest 게시글 작성 요청 DTO
     * @return 작성된 게시글 응답 DTO와 함께 HTTP 상태 코드 201을 반환합니다.
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        PostResponse response = postService.createPost(postCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 동일한 학교 수준의 게시글을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 게시글 목록과 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponse> responses = postService.getPosts(userId);
        return ResponseEntity.ok(responses);
    }
}
