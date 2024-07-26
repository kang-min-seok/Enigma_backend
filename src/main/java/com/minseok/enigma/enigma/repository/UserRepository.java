package com.minseok.enigma.enigma.repository;

import com.minseok.enigma.enigma.entity.SchoolLevel;
import com.minseok.enigma.enigma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}