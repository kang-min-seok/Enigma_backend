package com.minseok.enigma.enigma.repository;

import com.minseok.enigma.enigma.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostCategory 엔티티에 대한 Repository 인터페이스입니다.
 * 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    /**
     * 활성 상태의 카테고리 목록을 조회합니다.
     *
     * @param isActive 활성 상태
     * @return 활성 상태에 해당하는 카테고리 목록을 반환합니다.
     */
    List<PostCategory> findByIsActive(boolean isActive);

    /**
     * 특정 이름의 활성 상태를 조회합니다.
     *
     * @param code 카테고리 코드
     * @param isActive 활성 상태
     * @return 해당 이름과 활성 상태에 해당하는 카테고리
     */
    Optional<PostCategory> findByCodeAndIsActive(String code, boolean isActive);
}