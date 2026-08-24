package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.input.GamePlatformInput;
import com.janickmediadb.janickmediaapi.model.GamePlatformModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
