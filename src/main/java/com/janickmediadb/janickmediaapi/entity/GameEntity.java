package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GameGenreXrefEntity;
import com.janickmediadb.janickmediaapi.entity.xref.GamePlatformXrefEntity;
import com.janickmediadb.janickmediaapi.input.GameInput;
import com.janickmediadb.janickmediaapi.model.GameModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

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
    private Instant createdAt;
    private Instant lastUpdated;

    @ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "game")
    private Set<GameGenreXrefEntity> gameGenreXrefs = new HashSet<>();

    @OneToMany(mappedBy = "game")
    private Set<GamePlatformXrefEntity> gamePlatformXrefs = new HashSet<>();

    public void fromInput(GameInput gameInput) {
        this.name = gameInput.getName();
        this.description = gameInput.getDescription();
        this.year = gameInput.getYear();
    }

    public GameModel toModel(String timezone) {
        GameModel model = new GameModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setYear(year);
        model.setPosterFilepath(posterFilepath);
        model.setCreatedAt(createdAt.atZone(ZoneId.of(timezone)));
        model.setLastUpdated(lastUpdated.atZone(ZoneId.of(timezone)));
        model.setUser(user.toModel());
        return model;
    }
}
