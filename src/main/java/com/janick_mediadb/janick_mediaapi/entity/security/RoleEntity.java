package com.janick_mediadb.janick_mediaapi.entity.security;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.model.RoleModel;
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

    private String name;

    public RoleModel toModel() {
        RoleModel model = new RoleModel();
        model.setId(id);
        model.setName(name);
        return model;
    }
}
