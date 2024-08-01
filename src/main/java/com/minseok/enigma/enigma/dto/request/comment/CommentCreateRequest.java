package com.minseok.enigma.enigma.dto.request.comment;

import com.minseok.enigma.enigma.entity.SchoolLevel;
import lombok.Data;

/**
 * 댓글 생성 요청을 위한 DTO 클래스입니다.
 */
@Data
public class CommentCreateRequest {
    private Long postId;
    private Long userId;
    private String content;
    private SchoolLevel schoolLevel;
}