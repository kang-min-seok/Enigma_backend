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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup_Success() {
        SignupRequest request = SignupRequest.builder()
                .userName("testUser")
                .password("Test@1234")
                .email("test@example.com")
                .schoolLevel("HIGH")
                .grade(1)
                .build();

        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        authService.signup(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignup_UserAlreadyExists() {
        SignupRequest request = SignupRequest.builder()
                .userName("testUser")
                .password("Test@1234")
                .email("test@example.com")
                .schoolLevel("HIGH")
                .grade(1)
                .build();

        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signup(request);
        });

        assertEquals(ErrorCode.USER_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    public void testSignup_EmailAlreadyExists() {
        SignupRequest request = SignupRequest.builder()
                .userName("testUser")
                .password("Test@1234")
                .email("test@example.com")
                .schoolLevel("HIGH")
                .grade(1)
                .build();

        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signup(request);
        });

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    public void testSignup_InvalidPassword() {
        SignupRequest request = SignupRequest.builder()
                .userName("testUser")
                .password("invalid")
                .email("test@example.com")
                .schoolLevel("HIGH")
                .grade(1)
                .build();

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signup(request);
        });

        assertEquals(ErrorCode.INVALID_PASSWORD.getMessage(), exception.getMessage());
    }

    @Test
    public void testLogin_Success() {
        // 회원가입 절차
        SignupRequest signupRequest = SignupRequest.builder()
                .userName("testUser")
                .password("Test@1234")
                .email("test@example.com")
                .schoolLevel("HIGH")
                .grade(1)
                .build();

        String encodedPassword = passwordEncoder.encode("Test@1234");
        User user = User.builder()
                .id(1L)
                .userName("testUser")
                .password(encodedPassword)
                .email("test@example.com")
                .schoolLevel(SchoolLevel.valueOf("HIGH"))
                .grade(1)
                .build();

        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        authService.signup(signupRequest);

        // 로그인 절차
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("testUser");
        loginRequest.setPassword("Test@1234");

        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(encodedPassword))).thenReturn(true);
        when(jwtTokenProvider.createToken(anyString(), anyLong())).thenReturn("token");

        LoginResponse response = authService.login(loginRequest);

        System.out.println("토큰값: " + response.getToken());
        assertNotNull(response);
        assertEquals("testUser", response.getUserName());
        assertEquals("token", response.getToken());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUserName("testUser");
        request.setPassword("wrongPassword");

        User user = User.builder()
                .userName("testUser")
                .password("encodedPassword")
                .build();

        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.login(request);
        });

        assertEquals(ErrorCode.INVALID_LOGIN_CREDENTIALS.getMessage(), exception.getMessage());
    }
}