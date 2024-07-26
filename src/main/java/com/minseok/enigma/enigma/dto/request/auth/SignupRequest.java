package com.minseok.enigma.enigma.dto.request.auth;

import lombok.Data;

/**
 * 회원 가입 요청을 위한 DTO 클래스입니다.
 */
@Data
public class SignupRequest {
    private String userName;
    private String password;
    private String email;
    private String schoolLevel;
    private int grade;
}