package com.minseok.enigma.enigma.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자 이름입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "사용자 이름 또는 비밀번호가 잘못되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상이며, 숫자, 문자, 특수문자를 포함해야 합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}