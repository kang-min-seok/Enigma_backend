package com.minseok.enigma.enigma.dto.response.auth;

import lombok.Data;

/**
 * 로그인 응답을 위한 DTO 클래스입니다.
 */
@Data
public class LoginResponse {
    private Long userId;
    private String userName;
    private String token;
}