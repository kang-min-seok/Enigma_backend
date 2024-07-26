package com.minseok.enigma.enigma.dto.request.auth;

import lombok.Data;

/**
 * 로그인 요청을 위한 DTO 클래스입니다.
 */
@Data
public class LoginRequest {
    private String userName;
    private String password;
}