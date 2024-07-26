package com.minseok.enigma.enigma.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 커뮤니티 사용자를 나타내는 클래스입니다.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * 사용자의 고유 ID입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자의 이름(아이디)입니다.
     */
    @Column(nullable = false, unique = true)
    private String userName;

    /**
     * 사용자의 비밀번호입니다.
     */
    @Column(nullable = false)
    private String password;

    /**
     * 사용자의 이메일입니다.
     */
    @Column(nullable = false, unique = true)
    private String email;


    /**
     * 사용자의 학교 수준입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolLevel schoolLevel;

    /**
     * 사용자의 학년입니다.
     */
    @Column(nullable = false)
    private int grade;

    /**
     * 사용자가 생성된 날짜와 시간입니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 사용자가 마지막으로 수정된 날짜와 시간입니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 사용자의 친구 목록입니다.
     */
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    /**
     * 새로운 User 인스턴스를 생성합니다.
     *
     * @param userName 사용자의 이름(아이디)
     * @param password 사용자의 비밀번호
     * @param email 사용자의 이메일
     * @param schoolLevel 사용자의 학교 수준
     * @param grade 사용자의 학년
     */
    @Builder
    public User(String userName, String password, String email, SchoolLevel schoolLevel, int grade) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.schoolLevel = schoolLevel;
        this.grade = grade;
        this.friends = new HashSet<>();
    }

    /**
     * 친구를 추가합니다.
     *
     * @param friend 추가할 친구
     */
    public void addFriend(User friend) {
        friends.add(friend);
    }

    /**
     * 친구를 제거합니다.
     *
     * @param friend 제거할 친구
     */
    public void removeFriend(User friend) {
        friends.remove(friend);
    }

}