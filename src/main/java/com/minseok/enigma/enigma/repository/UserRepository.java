package com.minseok.enigma.enigma.repository;

import com.minseok.enigma.enigma.entity.SchoolLevel;
import com.minseok.enigma.enigma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 특정 학교 수준의 사용자 목록을 조회합니다.
     *
     * @param schoolLevel 학교 수준
     * @return 해당 학교 수준의 사용자 목록을 반환합니다.
     */
    List<User> findBySchoolLevel(SchoolLevel schoolLevel);

    /**
     * 특정 학교 수준과 학년의 사용자 목록을 조회합니다.
     *
     * @param schoolLevel 학교 수준
     * @param grade 학년
     * @return 해당 학교 수준과 학년의 사용자 목록을 반환합니다.
     */
    List<User> findBySchoolLevelAndGrade(SchoolLevel schoolLevel, int grade);

    /**
     * 특정 유저의 친구 목록을 조회합니다.
     *
     * @param user 특정 유저
     * @return 해당 유저의 친구 목록을 반환합니다.
     */
    List<User> findFriendsByUser(User user);

    /**
     * 사용자 이름으로 사용자를 조회합니다.
     *
     * @param userName 사용자 이름
     * @return 해당 이름의 사용자
     */
    Optional<User> findByUserName(String userName);

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 이메일 주소
     * @return 해당 이메일의 사용자
     */
    Optional<User> findByEmail(String email);

    /**
     * 사용자 이름이 존재하는지 확인합니다.
     *
     * @param userName 사용자 이름
     * @return 사용자 이름 존재 여부
     */
    boolean existsByUserName(String userName);

    /**
     * 이메일이 존재하는지 확인합니다.
     *
     * @param email 이메일 주소
     * @return 이메일 존재 여부
     */
    boolean existsByEmail(String email);
}