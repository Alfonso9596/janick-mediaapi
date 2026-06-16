package com.janick_mediadb.janick_mediaapi.entity.security;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.model.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = UsersEntity.USER_TABLE_NAME)
public class UsersEntity extends AbstractEntity {

    static final String USER_TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles_xref",
        joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private Set<RoleEntity> roles;

    private boolean isEnabled;

    public UserModel toModel() {
        UserModel model = new UserModel();
        model.setId(id);
        model.setUsername(username);
        model.setRoles(roles.stream().map(r -> r.getName().name()).toList());
        model.setEnabled(isEnabled);
        return model;
    }
}
