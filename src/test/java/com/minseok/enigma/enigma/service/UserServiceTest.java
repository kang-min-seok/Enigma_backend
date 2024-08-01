package com.minseok.enigma.enigma.service;

import com.minseok.enigma.enigma.dto.request.user.UserUpdateRequest;
import com.minseok.enigma.enigma.dto.response.user.UserResponse;
import com.minseok.enigma.enigma.entity.SchoolLevel;
import com.minseok.enigma.enigma.entity.User;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import com.minseok.enigma.enigma.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateUser_Success() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("newPassword");
        request.setEmail("newemail@example.com");
        request.setSchoolLevel(SchoolLevel.MIDDLE.toString());
        request.setGrade(2);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setEmail("oldemail@example.com");
        user.setSchoolLevel(SchoolLevel.HIGH);
        user.setGrade(3);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        assertEquals("newemail@example.com", response.getEmail());
        assertEquals(SchoolLevel.MIDDLE.toString(), response.getSchoolLevel());
        assertEquals(2, response.getGrade());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UserUpdateRequest request = new UserUpdateRequest();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUser(1L, request);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }


    @Test
    public void testGetFriends_Success() {

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setEmail("oldemail@example.com");
        user.setSchoolLevel(SchoolLevel.HIGH);
        user.setGrade(3);

        User friend1 = new User();
        friend1.setId(2L);
        friend1.setUserName("friendUser1");
        friend1.setEmail("friend1@example.com");
        friend1.setSchoolLevel(SchoolLevel.HIGH);
        friend1.setGrade(1);

        User friend2 = new User();
        friend2.setId(3L);
        friend2.setUserName("friendUser2");
        friend2.setEmail("friend2@example.com");
        friend2.setSchoolLevel(SchoolLevel.HIGH);
        friend2.setGrade(2);

        Set<User> friends = new HashSet<>();
        friends.add(friend1);
        friends.add(friend2);
        user.setFriends(friends);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<UserResponse> responses = userService.getFriends(1L);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("friendUser1", responses.get(0).getUserName());
        assertEquals("friendUser2", responses.get(1).getUserName());
    }

    @Test
    public void testUpdateUser_EmailAlreadyExists() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("existingemail@example.com");

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUser(1L, request);
        });

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setSchoolLevel(SchoolLevel.HIGH);
        user.setGrade(3);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserResponse response = userService.getUser(1L);

        assertNotNull(response);
        assertEquals("testUser", response.getUserName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(SchoolLevel.HIGH.toString(), response.getSchoolLevel());
        assertEquals(3, response.getGrade());
    }

    @Test
    public void testGetUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.getUser(1L);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetUsersBySchoolLevelAndGrade() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("user1");
        user1.setSchoolLevel(SchoolLevel.HIGH);
        user1.setGrade(3);

        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("user2");
        user2.setSchoolLevel(SchoolLevel.HIGH);
        user2.setGrade(3);

        when(userRepository.findBySchoolLevelAndGrade(any(SchoolLevel.class), anyInt()))
                .thenReturn(Arrays.asList(user1, user2));

        List<UserResponse> responses = userService.getUsersBySchoolLevelAndGrade(SchoolLevel.HIGH, 3);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("user1", responses.get(0).getUserName());
        assertEquals("user2", responses.get(1).getUserName());
    }

    @Test
    public void testAddFriend_Success() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");

        User friend = new User();
        friend.setId(2L);
        friend.setUserName("friendUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        userService.addFriend(1L, 2L);

        verify(userRepository, times(1)).save(user);
        assertTrue(user.getFriends().contains(friend));
    }

    @Test
    public void testRemoveFriend_Success() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");

        User friend = new User();
        friend.setId(2L);
        friend.setUserName("friendUser");

        user.addFriend(friend);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        userService.removeFriend(1L, 2L);

        verify(userRepository, times(1)).save(user);
        assertFalse(user.getFriends().contains(friend));
    }
}
