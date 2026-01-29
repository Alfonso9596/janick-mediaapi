package com.janick_mediadb.janick_mediaapi.entity.security;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = RoleEntity.ROLES_TABLE_NAME)
public class RoleEntity extends AbstractEntity {

    static final String ROLES_TABLE_NAME = "roles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private ERole name;
}
