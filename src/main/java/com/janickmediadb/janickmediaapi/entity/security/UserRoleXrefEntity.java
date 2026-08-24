package com.janickmediadb.janickmediaapi.entity.security;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
