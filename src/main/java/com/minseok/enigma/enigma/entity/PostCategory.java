package com.minseok.enigma.enigma.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


/**
 * 커뮤니티 게시글의 카테고리를 나타내는 클래스입니다.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCategory {

    /**
     * 카테고리의 고유 ID입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 카테고리 코드로, 고유해야 합니다.
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 카테고리의 이름입니다.
     */
    @Column(nullable = false)
    private String name;

    /**
     * 카테고리의 설명입니다.
     */
    private String description;

    /**
     * 카테고리가 활성 상태인지 여부를 나타냅니다.
     */
    @Column(nullable = false)
    private boolean isActive = true;

    /**
     * 이 카테고리에 속한 게시글의 리스트입니다.
     */
    @OneToMany(mappedBy = "postCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;
}