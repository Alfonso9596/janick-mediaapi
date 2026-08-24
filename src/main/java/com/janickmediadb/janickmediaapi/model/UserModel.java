package com.janickmediadb.janickmediaapi.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel extends AbstractModel {

    private int id;
    private String username;
    private List<String> roles;
    private boolean isEnabled;
}
