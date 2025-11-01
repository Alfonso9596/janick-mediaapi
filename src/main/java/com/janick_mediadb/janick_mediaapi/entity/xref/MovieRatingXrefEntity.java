package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = MovieRatingXrefEntity.MOVIE_RATING_TABLE_NAME)
public class MovieRatingXrefEntity extends AbstractEntity {

    static final String MOVIE_RATING_TABLE_NAME = "movie_rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = MovieEntity.class)
    @JoinColumn(name = "MOVIE_ID")
    private MovieEntity movie;

    private int rating;
}
