package com.minseok.enigma.enigma.controller;

import com.minseok.enigma.enigma.dto.request.user.UserUpdateRequest;
import com.minseok.enigma.enigma.dto.response.user.UserResponse;
import com.minseok.enigma.enigma.entity.SchoolLevel;
import com.minseok.enigma.enigma.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param userId 사용자 ID
     * @param userUpdateRequest 사용자 정보 업데이트 요청 DTO
     * @return 업데이트된 사용자 응답 DTO와 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse response = userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO와 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse response = userService.getUser(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 학교 수준과 학년의 사용자 목록을 조회합니다.
     *
     * @param schoolLevel 학교 수준
     * @param grade 학년
     * @return 사용자 목록과 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @GetMapping("/school-level/{schoolLevel}/grade/{grade}")
    public ResponseEntity<List<UserResponse>> getUsersBySchoolLevelAndGrade(@PathVariable String schoolLevel, @PathVariable int grade) {
        List<UserResponse> responses = userService.getUsersBySchoolLevelAndGrade(SchoolLevel.valueOf(schoolLevel.toUpperCase()), grade);
        return ResponseEntity.ok(responses);
    }

    /**
     * 사용자에게 친구를 추가합니다.
     *
     * @param userId 사용자 ID
     * @param friendId 친구 ID
     * @return HTTP 상태 코드 200을 반환합니다.
     */
    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자에게서 친구를 제거합니다.
     *
     * @param userId 사용자 ID
     * @param friendId 친구 ID
     * @return HTTP 상태 코드 200을 반환합니다.
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자의 친구 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 친구 목록 응답 DTO 리스트와 함께 HTTP 상태 코드 200을 반환합니다.
     */
    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UserResponse>> getFriends(@PathVariable Long userId) {
        List<UserResponse> responses = userService.getFriends(userId);
        return ResponseEntity.ok(responses);
    }
}
