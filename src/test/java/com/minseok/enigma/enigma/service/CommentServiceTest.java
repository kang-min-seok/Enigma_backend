package com.minseok.enigma.enigma.service;

import com.minseok.enigma.enigma.dto.request.comment.CommentCreateRequest;
import com.minseok.enigma.enigma.dto.response.comment.CommentResponse;
import com.minseok.enigma.enigma.entity.*;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import com.minseok.enigma.enigma.repository.CommentRepository;
import com.minseok.enigma.enigma.repository.PostRepository;
import com.minseok.enigma.enigma.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateComment_Success() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setPostId(1L);
        request.setUserId(1L);
        request.setContent("Test Comment");
        request.setSchoolLevel(SchoolLevel.HIGH);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setId(1L);
            return savedComment;
        });

        CommentResponse response = commentService.createComment(request);

        assertNotNull(response);
        assertEquals("Test Comment", response.getContent());
        assertEquals("testUser", response.getUserName());
        assertEquals(1L, response.getPostId());
        assertEquals(SchoolLevel.HIGH.toString(), response.getSchoolLevel());
    }

    @Test
    public void testCreateComment_UserNotFound() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setPostId(1L);
        request.setUserId(1L);
        request.setContent("Test Comment");
        request.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.createComment(request);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void testCreateComment_PostNotFound() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setPostId(1L);
        request.setUserId(1L);
        request.setContent("Test Comment");
        request.setSchoolLevel(SchoolLevel.HIGH);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.createComment(request);
        });

        assertEquals(ErrorCode.POST_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void testCreateComment_InvalidAccess() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setPostId(1L);
        request.setUserId(1L);
        request.setContent("Test Comment");
        request.setSchoolLevel(SchoolLevel.MIDDLE);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.createComment(request);
        });

        assertEquals(ErrorCode.INVALID_ACCESS.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetCommentsByPost_Success() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setSchoolLevel(SchoolLevel.HIGH);

        Comment comment1 = Comment.builder()
                .id(1L)
                .content("Comment 1")
                .user(user)
                .post(post)
                .schoolLevel(SchoolLevel.HIGH)
                .status(Status.ACTIVE)
                .build();

        Comment comment2 = Comment.builder()
                .id(2L)
                .content("Comment 2")
                .user(user)
                .post(post)
                .schoolLevel(SchoolLevel.HIGH)
                .status(Status.ACTIVE)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findByPostAndStatus(any(Post.class), any(Status.class))).thenReturn(Arrays.asList(comment1, comment2));

        List<CommentResponse> responses = commentService.getCommentsByPost(1L, 1L);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Comment 1", responses.get(0).getContent());
        assertEquals("Comment 2", responses.get(1).getContent());
    }

    @Test
    public void testGetCommentsByPost_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.getCommentsByPost(1L, 1L);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetCommentsByPost_PostNotFound() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.getCommentsByPost(1L, 1L);
        });

        assertEquals(ErrorCode.POST_NOT_FOUND.getMessage(), exception.getMessage());
    }
}
