package com.minseok.enigma.enigma.service;


import com.minseok.enigma.enigma.dto.request.auth.LoginRequest;
import com.minseok.enigma.enigma.dto.request.auth.SignupRequest;
import com.minseok.enigma.enigma.dto.response.auth.LoginResponse;
import com.minseok.enigma.enigma.entity.SchoolLevel;
import com.minseok.enigma.enigma.entity.User;
import com.minseok.enigma.enigma.repository.UserRepository;
import com.minseok.enigma.enigma.security.JwtTokenProvider;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;


/**
 * 사용자 인증 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);


    /**
     * 회원 가입을 처리합니다.
     *
     * @param signupRequest 회원 가입 요청 DTO
     */
    @Transactional
    public void signup(SignupRequest signupRequest) {
        if (userRepository.existsByUserName(signupRequest.getUserName())) {
            throw new CustomException("이미 존재하는 사용자 이름입니다.", ErrorCode.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException("이미 존재하는 이메일입니다.", ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 정책 검증
        if (!isPasswordValid(signupRequest.getPassword())) {
            throw new CustomException("비밀번호는 8자 이상이며, 숫자, 문자, 특수문자를 포함해야 합니다.", ErrorCode.INVALID_PASSWORD);
        }

        User user = User.builder()
                .userName(signupRequest.getUserName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .email(signupRequest.getEmail())
                .schoolLevel(SchoolLevel.valueOf(signupRequest.getSchoolLevel().toUpperCase()))
                .grade(signupRequest.getGrade())
                .build();

        userRepository.save(user);
    }

    /**
     * 비밀번호의 유효성을 검증합니다.
     *
     * @param password 검증할 비밀번호
     * @return 비밀번호가 유효하면 true, 그렇지 않으면 false
     */
    private boolean isPasswordValid(String password) {
        return pattern.matcher(password).matches();
    }

    /**
     * 로그인을 처리하고 JWT 토큰을 반환합니다.
     *
     * @param loginRequest 로그인 요청 DTO
     * @return 로그인 응답 DTO
     */
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new CustomException("사용자 이름 또는 비밀번호가 잘못되었습니다.", ErrorCode.INVALID_LOGIN_CREDENTIALS));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException("사용자 이름 또는 비밀번호가 잘못되었습니다.", ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        String token = jwtTokenProvider.createToken(user.getUserName(), user.getId());

        return LoginResponse.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .token(token)
                .build();
    }

    /**
     * 사용자 이름으로 사용자를 조회합니다.
     *
     * @param userName 사용자 이름
     * @return 사용자 엔티티
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}