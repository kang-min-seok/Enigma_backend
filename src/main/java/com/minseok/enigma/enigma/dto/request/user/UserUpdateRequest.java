package com.minseok.enigma.enigma.dto.request.user;

import lombok.Data;

/**
 * 사용자 정보 업데이트 요청을 위한 DTO 클래스입니다.
 */
@Data
public class UserUpdateRequest {
    private String password;
    private String email;
    private String schoolLevel;
    private int grade;
}