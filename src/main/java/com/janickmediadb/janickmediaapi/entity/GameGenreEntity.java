package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.input.GenreInput;
import com.janickmediadb.janickmediaapi.model.GenreModel;
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
@Table(name = GameGenreEntity.GAME_GENRE_TABLE_NAME)
public class GameGenreEntity extends AbstractEntity {

    static final String GAME_GENRE_TABLE_NAME = "game_genre";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public void fromInput(GenreInput genreInput) {
        this.name = genreInput.getName();
    }

    public GenreModel toModel() {
        GenreModel model = new GenreModel();
        model.setId(id);
        model.setName(name);
        return model;
    }

    public static List<String> toModels(List<GameGenreEntity> genres) {
        return genres.stream().map(GameGenreEntity::getName).toList();
    }
}
