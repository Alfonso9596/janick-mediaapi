package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.input.GamePlatformInput;
import com.janick_mediadb.janick_mediaapi.model.GamePlatformModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = GamePlatformEntity.GAME_PLATFORM_TABLE_NAME)
public class GamePlatformEntity extends AbstractEntity {

    static final String GAME_PLATFORM_TABLE_NAME = "game_platform";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public void fromInput(GamePlatformInput gamePlatformInput) {
        this.name = gamePlatformInput.getName();
    }

    public GamePlatformModel toModel() {
        GamePlatformModel model = new GamePlatformModel();
        model.setId(id);
        model.setName(name);
        return model;
    }

    public static List<String> toModels(List<GamePlatformEntity> platforms) {
        return platforms.stream().map(GamePlatformEntity::getName).toList();
    }
}
