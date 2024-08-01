package com.minseok.enigma.enigma.dto.response.comment;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 댓글 응답을 위한 DTO 클래스입니다.
 */
@Data
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private String schoolLevel;
}