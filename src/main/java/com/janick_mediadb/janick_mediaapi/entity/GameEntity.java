package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.entity.xref.GameGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.GamePlatformXrefEntity;
import com.janick_mediadb.janick_mediaapi.input.GameInput;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = GameEntity.GAME_TABLE_NAME)
public class GameEntity extends AbstractEntity {

    static final String GAME_TABLE_NAME = "game";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob
    @Column(columnDefinition = "text")
    private String description;
    @Column(length = 4)
    private String year;
    private String posterFilepath;

    @OneToMany(mappedBy = "game")
    private Set<GameGenreXrefEntity> gameGenreXrefs = new HashSet<>();

    @OneToMany(mappedBy = "game")
    private Set<GamePlatformXrefEntity> gamePlatformXrefs = new HashSet<>();

    public void fromInput(GameInput gameInput) {
        this.name = gameInput.getName();
        this.description = gameInput.getDescription();
        this.year = gameInput.getYear();
    }

    public GameModel toModel() {
        GameModel model = new GameModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setYear(year);
        model.setPosterFilepath(posterFilepath);
        return model;
    }
}
