package com.minseok.enigma.enigma.repository;

import com.minseok.enigma.enigma.entity.Post;
import com.minseok.enigma.enigma.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Post 엔티티에 대한 Repository 인터페이스입니다.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 활성화된 게시글을 모두 조회합니다.
     *
     * @param status 게시글의 상태
     * @return 상태에 해당하는 게시글 목록
     */
    List<Post> findByStatus(Status status);

    /**
     * 특정 작성자의 활성화된 게시글 목록을 조회합니다.
     *
     * @param authorId 작성자의 ID
     * @param status 게시글의 상태
     * @return 작성자에 해당하고 상태가 ACTIVE인 게시글 목록
     */
    List<Post> findByAuthorId(Long authorId, Status status);

    /**
     * 특정 카테고리의 활성화된 게시글 목록을 조회합니다.
     *
     * @param categoryId 카테고리의 ID
     * @param status 게시글의 상태
     * @return 카테고리에 해당하고 상태가 ACTIVE인 게시글 목록
     */
    List<Post> findByPostCategoryId(Long categoryId, Status status);


    /**
     * 모든 게시글 목록을 페이징하여 조회합니다.
     *
     * @param status 게시글의 상태
     * @param pageable 페이징 정보
     * @return 상태에 해당하는 게시글 목록 페이지
     */
    Page<Post> findByStatus(Status status, Pageable pageable);

    /**
     * 특정 작성자의 게시글 목록을 페이징하여 조회합니다.
     *
     * @param authorId 작성자의 ID
     * @param status 게시글의 상태
     * @param pageable 페이징 정보
     * @return 작성자에 해당하는 게시글 목록 페이지
     */
    Page<Post> findByAuthorId(Long authorId, Status status, Pageable pageable);

    /**
     * 특정 카테고리의 게시글 목록을 페이징하여 조회합니다.
     *
     * @param categoryId 카테고리의 ID
     * @param status 게시글의 상태
     * @param pageable 페이징 정보
     * @return 카테고리에 해당하는 게시글 목록 페이지
     */
    Page<Post> findByPostCategoryId(Long categoryId, Status status, Pageable pageable);
}