package com.janick_mediadb.janick_mediaapi.entity.security;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = RefreshTokenEntity.REFRESH_TOKEN_TABLE_NAME)
public class RefreshTokenEntity extends AbstractEntity {

    static final String REFRESH_TOKEN_TABLE_NAME = "refreshtoken";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UsersEntity user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
