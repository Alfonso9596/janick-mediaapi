package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.model.MovieGenreModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public void fromInput(MovieGenreInput movieGenreInput) {
        this.name = movieGenreInput.getName();
    }

    public MovieGenreModel toModel() {
        MovieGenreModel model = new MovieGenreModel();
        model.setId(id);
        model.setName(name);
        return model;
    }

    public static List<MovieGenreModel> toModels(List<GameGenreEntity> genres) {
        List<MovieGenreModel> models = new ArrayList<>();
        for (GameGenreEntity genre : genres) {
            models.add(genre.toModel());
        }

        return models;
    }
}
