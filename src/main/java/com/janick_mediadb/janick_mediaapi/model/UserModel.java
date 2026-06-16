package com.janick_mediadb.janick_mediaapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserModel extends AbstractModel {

    private int id;
    private String username;
    private List<String> roles;
    private boolean isEnabled;
}
