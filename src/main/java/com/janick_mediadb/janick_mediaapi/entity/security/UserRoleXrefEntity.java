package com.janick_mediadb.janick_mediaapi.entity.security;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = UserRoleXrefEntity.USER_ROLE_TABLE_NAME)
public class UserRoleXrefEntity extends AbstractEntity {

    static final String USER_ROLE_TABLE_NAME = "users_roles_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = UsersEntity.class)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @ManyToOne(targetEntity = RoleEntity.class)
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity role;
}
