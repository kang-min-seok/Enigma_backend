package com.minseok.enigma.enigma.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 게시글에 대한 댓글을 나타내는 클래스입니다.
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    /**
     * 댓글의 고유 ID입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글 작성자입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 댓글이 달린 게시글입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 댓글의 내용입니다.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 댓글이 생성된 날짜와 시간입니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 댓글이 마지막으로 수정된 날짜와 시간입니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 댓글의 상태를 나타내는 열거형입니다.
     */
    public enum Status {
        ACTIVE, DELETED
    }

    /**
     * 댓글의 현재 상태입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * 새로운 Comment 인스턴스를 생성합니다.
     *
     * @param user 댓글 작성자
     * @param post 댓글이 달린 게시글
     * @param content 댓글 내용
     */
    @Builder
    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.status = Status.ACTIVE;
    }

    /**
     * 댓글을 삭제 상태로 변경합니다.
     */
    public void delete() {
        this.status = Status.DELETED;
    }
}