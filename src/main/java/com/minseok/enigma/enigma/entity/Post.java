package com.minseok.enigma.enigma.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 커뮤니티 게시글을 나타내는 클래스입니다.
 */
@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    /**
     * 게시글의 고유 ID입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 게시글의 제목입니다.
     */
    @Column(nullable = false)
    private String title;

    /**
     * 게시글의 내용입니다.
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 게시글 작성자입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    /**
     * 게시글의 조회수입니다.
     */
    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    /**
     * 게시글이 생성된 날짜와 시간입니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 게시글이 마지막으로 수정된 날짜와 시간입니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /**
     * 게시글의 현재 상태입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * 게시글이 속한 카테고리입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PostCategory postCategory;

    /**
     * 게시글의 학교 수준입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolLevel schoolLevel;

    /**
     * 새로운 게시글 인스턴스를 생성합니다.
     *
     * @param title 게시글의 제목
     * @param content 게시글의 내용
     * @param author 게시글 작성자
     * @param postCategory 게시글이 속한 카테고리
     * @param schoolLevel 게시글의 학교 수준
     */
    @Builder
    public Post(String title, String content, User author, PostCategory postCategory, SchoolLevel schoolLevel) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.postCategory = postCategory;
        this.schoolLevel = schoolLevel;
        this.viewCount = 0;
        this.status = Status.ACTIVE;
    }

    /**
     * 게시글을 삭제 상태로 변경합니다.
     */
    public void delete() {
        this.status = Status.DELETED;
    }

    /**
     * 게시글의 조회수를 1 증가시킵니다.
     */
    public void incrementViewCount() {
        this.viewCount++;
    }
}