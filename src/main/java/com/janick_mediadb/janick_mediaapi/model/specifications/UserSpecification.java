package com.janick_mediadb.janick_mediaapi.model.specifications;

import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {
        throw new IllegalStateException("Non-constructor class");
    }

    public static Specification<UsersEntity> likeUsername(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("username"), "%" + username + "%");
    }

    public static Specification<UsersEntity> containsRole(String role) {
        return (root, query, criteriaBuilder) -> {
            Join<UsersEntity, RoleEntity> usersRoleJoin = root.join("users_roles_xref", JoinType.LEFT).join("ROLE_ID", JoinType.LEFT);
            return  criteriaBuilder.like(usersRoleJoin.get("name"), "%" + role + "%");
        };
    }
}
