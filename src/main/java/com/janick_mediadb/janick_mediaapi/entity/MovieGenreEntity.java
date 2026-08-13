package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.input.GenreInput;
import com.janick_mediadb.janick_mediaapi.model.GenreModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = MovieGenreEntity.MOVIE_GENRE_TABLE_NAME)
public class MovieGenreEntity extends AbstractEntity {

    static final String MOVIE_GENRE_TABLE_NAME = "movie_genre";

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

    public static List<String> toModels(List<MovieGenreEntity> genres) {
        return genres.stream().map(MovieGenreEntity::getName).toList();
    }
}
