package com.mbit.api.user.domain;

import com.mbit.api.common.BaseTimeEntity;
import com.mbit.api.common.constant.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '사용자 이메일'")
    private String email;

    @Column(columnDefinition = "varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '사용자 닉네임'")
    private String nickname;

    @Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '사용자 비밀번호'")
    @Lob
    private String password;

    @Column(columnDefinition = "tinytext not null COMMENT '권한'")
    @Builder.Default
    private List<UserRole> userRole = new ArrayList<>();

    @Builder
    public User(String email, String nickname, String password, List<UserRole> userRole) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }

    public static User initEmailUser(String email, String password, String nickname, String userRole) {
        String role = "ROLE_ADMIN";
        if (!role.equals(userRole)) {
            role = "ROLE_USER";
        }

        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .userRole(List.of(UserRole.valueOf(role)))
                .build();
    }

    public String rolesToString() {
        return this.userRole.stream().map(UserRole::getValue)
                .collect(Collectors.joining(","));
    }
}
