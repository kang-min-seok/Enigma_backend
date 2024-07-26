package com.minseok.enigma.enigma.dto.request.post;

import lombok.Data;

/**
 * 게시글 생성 요청을 위한 DTO 클래스입니다.
 */
@Data
public class PostCreateRequest {
    private String title;
    private String content;
    private Long authorId;
    private Long categoryId;
}