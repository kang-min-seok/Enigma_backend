package com.minseok.enigma.enigma.controller;

import com.minseok.enigma.enigma.dto.request.auth.LoginRequest;
import com.minseok.enigma.enigma.dto.request.auth.SignupRequest;
import com.minseok.enigma.enigma.dto.response.auth.LoginResponse;
import com.minseok.enigma.enigma.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 사용자 인증 관련 요청을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 가입 요청을 처리합니다.
     *
     * @param signupRequest 회원 가입 요청 DTO
     * @return 성공 시 HTTP 상태 코드 201을 반환합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 로그인 요청을 처리하고 JWT 토큰을 반환합니다.
     *
     * @param loginRequest 로그인 요청 DTO
     * @return 로그인 응답 DTO와 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
