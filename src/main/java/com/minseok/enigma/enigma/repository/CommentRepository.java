package com.minseok.enigma.enigma.repository;

import com.minseok.enigma.enigma.entity.Comment;
import com.minseok.enigma.enigma.entity.Post;
import com.minseok.enigma.enigma.entity.Status;
import com.minseok.enigma.enigma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Comment 엔티티에 대한 Repository 인터페이스입니다.
 * 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글에 달린 댓글 목록을 조회합니다.
     *
     * @param post 댓글이 달린 게시글
     * @return 해당 게시글에 달린 댓글 목록
     */
    List<Comment> findByPost(Post post);

    /**
     * 특정 게시글에 달린 활성 상태의 댓글 목록을 조회합니다.
     *
     * @param post 댓글이 달린 게시글
     * @param status 댓글의 상태
     * @return 해당 게시글에 달린 활성 상태의 댓글 목록
     */
    List<Comment> findByPostAndStatus(Post post, Status status);

    /**
     * 특정 사용자가 작성한 댓글 목록을 조회합니다.
     *
     * @param user 댓글 작성자
     * @return 해당 사용자가 작성한 댓글 목록
     */
    List<Comment> findByUser(User user);

    /**
     * 특정 사용자가 작성한 활성 상태의 댓글 목록을 조회합니다.
     *
     * @param user 댓글 작성자
     * @param status 댓글의 상태
     * @return 해당 사용자가 작성한 활성 상태의 댓글 목록
     */
    List<Comment> findByUserAndStatus(User user, Status status);
}