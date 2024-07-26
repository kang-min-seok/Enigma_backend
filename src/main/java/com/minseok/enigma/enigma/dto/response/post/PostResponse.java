package com.minseok.enigma.enigma.dto.response.post;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 게시글 응답을 위한 DTO 클래스입니다.
 */
@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String categoryName;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
}