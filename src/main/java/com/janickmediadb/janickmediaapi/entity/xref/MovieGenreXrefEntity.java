package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
