package com.minseok.enigma.enigma.service;

import com.minseok.enigma.enigma.dto.request.post.PostCreateRequest;
import com.minseok.enigma.enigma.dto.response.post.PostResponse;
import com.minseok.enigma.enigma.entity.*;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import com.minseok.enigma.enigma.repository.PostCategoryRepository;
import com.minseok.enigma.enigma.repository.PostRepository;
import com.minseok.enigma.enigma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;

    /**
     * 게시글을 작성합니다.
     *
     * @param postCreateRequest 게시글 작성 요청 DTO
     * @return 작성된 게시글의 응답 DTO
     */
    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest) {
        User user = userRepository.findById(postCreateRequest.getAuthorId())
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        PostCategory postCategory = postCategoryRepository.findById(postCreateRequest.getCategoryId())
                .orElseThrow(() -> new CustomException("카테고리를 찾을 수 없습니다.", ErrorCode.CATEGORY_NOT_FOUND));

        // 동일한 schoolLevel인지 확인
        if (!user.getSchoolLevel().equals(postCreateRequest.getSchoolLevel())) {
            throw new CustomException("동일한 학교 수준에서만 활동할 수 있습니다.", ErrorCode.INVALID_ACCESS);
        }

        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .author(user)
                .status(Status.ACTIVE)
                .postCategory(postCategory)
                .schoolLevel(postCreateRequest.getSchoolLevel())
                .build();

        postRepository.save(post);

        return convertToPostResponse(post);
    }

    /**
     * 동일한 학교 수준의 모든 게시글을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 동일한 학교 수준의 게시글 목록
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        List<Post> posts = postRepository.findBySchoolLevel(user.getSchoolLevel());

        return posts.stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 엔티티를 PostResponse DTO로 변환합니다.
     *
     * @param post 게시글 엔티티
     * @return 게시글 응답 DTO
     */
    private PostResponse convertToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getUserName())
                .categoryName(post.getPostCategory().getName())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .status(post.getStatus().toString())
                .schoolLevel(post.getSchoolLevel().toString())
                .build();
    }
}
