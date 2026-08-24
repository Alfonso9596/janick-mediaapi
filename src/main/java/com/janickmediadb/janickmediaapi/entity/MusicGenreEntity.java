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
@Table(name = MusicGenreEntity.MUSIC_GENRE_TABLE_NAME)
public class MusicGenreEntity extends AbstractEntity {

    static final String MUSIC_GENRE_TABLE_NAME = "music_genre";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public void fromInput(GenreInput musicGenreInput) {
        this.name = musicGenreInput.getName();
    }

    public GenreModel toModel() {
        GenreModel model = new GenreModel();
        model.setId(id);
        model.setName(name);
        return model;
    }

    public static List<String> toModels(List<MusicGenreEntity> genres) {
        return genres.stream().map(MusicGenreEntity::getName).toList();
    }
}
