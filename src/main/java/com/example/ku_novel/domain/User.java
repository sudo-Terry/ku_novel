package com.example.ku_novel.domain;

import com.example.ku_novel.LocalDateTimeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "last_attendance")
    private LocalDateTime lastAttendance;

    @Builder.Default
    @Column(name = "point", nullable = false)
    private Integer point = 500;
}
