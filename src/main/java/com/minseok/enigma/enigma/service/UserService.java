package com.minseok.enigma.enigma.service;

import com.minseok.enigma.enigma.dto.request.user.UserUpdateRequest;
import com.minseok.enigma.enigma.dto.response.user.UserResponse;
import com.minseok.enigma.enigma.entity.SchoolLevel;
import com.minseok.enigma.enigma.entity.User;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import com.minseok.enigma.enigma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 사용자 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param userId            사용자 ID
     * @param userUpdateRequest 사용자 정보 업데이트 요청 DTO
     * @return 업데이트된 사용자 응답 DTO
     */
    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(userUpdateRequest.getEmail())) {
                throw new CustomException("이미 존재하는 이메일입니다.", ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            user.setEmail(userUpdateRequest.getEmail());
        }

        user.setSchoolLevel(SchoolLevel.valueOf(userUpdateRequest.getSchoolLevel().toUpperCase()));
        user.setGrade(userUpdateRequest.getGrade());

        userRepository.save(user);

        return convertToUserResponse(user);
    }

    /**
     * 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO
     */
    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        return convertToUserResponse(user);
    }

    /**
     * 특정 학교 수준과 학년의 사용자 목록을 조회합니다.
     *
     * @param schoolLevel 학교 수준
     * @param grade       학년
     * @return 해당 학교 수준과 학년의 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersBySchoolLevelAndGrade(SchoolLevel schoolLevel, int grade) {
        List<User> users = userRepository.findBySchoolLevelAndGrade(schoolLevel, grade);
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * 사용자에게 친구를 추가합니다.
     *
     * @param userId    사용자 ID
     * @param friendId 친구 ID
     */
    @Transactional
    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        user.addFriend(friend);
        userRepository.save(user);
    }

    /**
     * 사용자에게서 친구를 제거합니다.
     *
     * @param userId    사용자 ID
     * @param friendId 친구 ID
     */
    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        user.removeFriend(friend);
        userRepository.save(user);
    }

    /**
     * 사용자의 친구 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 친구 목록 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        return user.getFriends().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }


    /**
     * 사용자 엔티티를 UserResponse DTO로 변환합니다.
     *
     * @param user 사용자 엔티티
     * @return 사용자 응답 DTO
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .schoolLevel(user.getSchoolLevel().toString())
                .grade(user.getGrade())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
