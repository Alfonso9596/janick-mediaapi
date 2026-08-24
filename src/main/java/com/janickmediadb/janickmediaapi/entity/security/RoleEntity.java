package com.janickmediadb.janickmediaapi.entity.security;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.input.admin.RoleInput;
import com.janickmediadb.janickmediaapi.model.RoleModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public void fromInput(RoleInput roleInput) {
        this.name = roleInput.getName();
    }

    public RoleModel toModel() {
        RoleModel model = new RoleModel();
        model.setId(id);
        model.setName(name);
        return model;
    }
}
