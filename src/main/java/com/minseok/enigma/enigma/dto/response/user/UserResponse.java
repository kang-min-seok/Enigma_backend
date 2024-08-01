package com.minseok.enigma.enigma.dto.response.user;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 사용자 응답을 위한 DTO 클래스입니다.
 */
@Data
@Builder
public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private String schoolLevel;
    private int grade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}