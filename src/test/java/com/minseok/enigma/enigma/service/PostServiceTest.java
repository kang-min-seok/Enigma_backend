package com.minseok.enigma.enigma.service;

import com.minseok.enigma.enigma.dto.request.post.PostCreateRequest;
import com.minseok.enigma.enigma.dto.response.post.PostResponse;
import com.minseok.enigma.enigma.entity.*;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import com.minseok.enigma.enigma.repository.PostCategoryRepository;
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

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostCategoryRepository postCategoryRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost_Success() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setAuthorId(1L);
        request.setCategoryId(1L);
        request.setSchoolLevel(SchoolLevel.HIGH);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        PostCategory postCategory = new PostCategory();
        postCategory.setId(1L);
        postCategory.setName("General");
        postCategory.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postCategoryRepository.findById(anyLong())).thenReturn(Optional.of(postCategory));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostResponse response = postService.createPost(request);

        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
        assertEquals("testUser", response.getAuthorName());
        assertEquals("General", response.getCategoryName());
        assertEquals(SchoolLevel.HIGH.toString(), response.getSchoolLevel());
    }

    @Test
    public void testCreatePost_UserNotFound() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setAuthorId(1L);
        request.setCategoryId(1L);
        request.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.createPost(request);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void testCreatePost_CategoryNotFound() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setAuthorId(1L);
        request.setCategoryId(1L);
        request.setSchoolLevel(SchoolLevel.HIGH);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postCategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.createPost(request);
        });

        assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void testCreatePost_InvalidAccess() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setAuthorId(1L);
        request.setCategoryId(1L);
        request.setSchoolLevel(SchoolLevel.MIDDLE);

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        PostCategory postCategory = new PostCategory();
        postCategory.setId(1L);
        postCategory.setName("General");
        postCategory.setSchoolLevel(SchoolLevel.HIGH);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postCategoryRepository.findById(anyLong())).thenReturn(Optional.of(postCategory));

        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.createPost(request);
        });

        assertEquals(ErrorCode.INVALID_ACCESS.getMessage(), exception.getMessage());
    }

    @Test
    public void testGetPosts_Success() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setSchoolLevel(SchoolLevel.HIGH);

        PostCategory postCategory = new PostCategory();
        postCategory.setId(1L);
        postCategory.setName("General");
        postCategory.setSchoolLevel(SchoolLevel.HIGH);

        Post post1 = Post.builder()
                .id(1L)
                .title("Title 1")
                .content("Content 1")
                .author(user)
                .status(Status.ACTIVE)
                .postCategory(postCategory)
                .schoolLevel(SchoolLevel.HIGH)
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .title("Title 2")
                .content("Content 2")
                .author(user)
                .status(Status.ACTIVE)
                .postCategory(postCategory)
                .schoolLevel(SchoolLevel.HIGH)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findBySchoolLevel(any(SchoolLevel.class))).thenReturn(Arrays.asList(post1, post2));

        List<PostResponse> responses = postService.getPosts(1L);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Title 1", responses.get(0).getTitle());
        assertEquals("Title 2", responses.get(1).getTitle());
    }

    @Test
    public void testGetPosts_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.getPosts(1L);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }
}
