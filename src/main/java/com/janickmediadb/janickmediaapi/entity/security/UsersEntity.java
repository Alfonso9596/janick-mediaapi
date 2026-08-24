package com.janickmediadb.janickmediaapi.entity.security;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.model.UserModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "users_roles_xref",
        joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private Set<RoleEntity> roles;

    private boolean isEnabled;

    public UserModel toModel() {
        UserModel model = new UserModel();
        model.setId(id);
        model.setUsername(username);
        model.setRoles(roles.stream().map(RoleEntity::getName).toList());
        model.setEnabled(isEnabled);
        return model;
    }
}
