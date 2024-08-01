package com.minseok.enigma.enigma.service;

import com.minseok.enigma.enigma.dto.request.comment.CommentCreateRequest;
import com.minseok.enigma.enigma.dto.response.comment.CommentResponse;
import com.minseok.enigma.enigma.entity.*;
import com.minseok.enigma.enigma.exception.CustomException;
import com.minseok.enigma.enigma.exception.ErrorCode;
import com.minseok.enigma.enigma.repository.CommentRepository;
import com.minseok.enigma.enigma.repository.PostRepository;
import com.minseok.enigma.enigma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 댓글을 작성합니다.
     *
     * @param commentCreateRequest 댓글 작성 요청 DTO
     * @return 작성된 댓글의 응답 DTO
     */
    @Transactional
    public CommentResponse createComment(CommentCreateRequest commentCreateRequest) {
        User user = userRepository.findById(commentCreateRequest.getUserId())
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(commentCreateRequest.getPostId())
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", ErrorCode.POST_NOT_FOUND));

        // 동일한 schoolLevel인지 확인
        if (!user.getSchoolLevel().equals(commentCreateRequest.getSchoolLevel())) {
            throw new CustomException("동일한 학교 수준에서만 활동할 수 있습니다.", ErrorCode.INVALID_ACCESS);
        }

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentCreateRequest.getContent())
                .status(Status.ACTIVE)
                .schoolLevel(commentCreateRequest.getSchoolLevel())
                .build();

        commentRepository.save(comment);

        return convertToCommentResponse(comment);
    }

    /**
     * 특정 게시글에 달린 댓글을 조회합니다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @return 댓글 목록
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", ErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findByPostAndStatus(post, Status.ACTIVE);

        return comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 엔티티를 CommentResponse DTO로 변환합니다.
     *
     * @param comment 댓글 엔티티
     * @return 댓글 응답 DTO
     */
    private CommentResponse convertToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .status(comment.getStatus().toString())
                .schoolLevel(comment.getSchoolLevel().toString())
                .build();
    }
}
