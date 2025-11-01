package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = MovieGenreXrefEntity.MOVIE_GENRE_TABLE_NAME)
public class MovieGenreXrefEntity extends AbstractEntity {

    static final String MOVIE_GENRE_TABLE_NAME = "movie_genre_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = MovieEntity.class)
    @JoinColumn(name = "MOVIE_ID")
    private MovieEntity movie;

    @ManyToOne(targetEntity = MovieGenreEntity.class)
    @JoinColumn(name = "GENRE_ID")
    private MovieGenreEntity genre;
}
