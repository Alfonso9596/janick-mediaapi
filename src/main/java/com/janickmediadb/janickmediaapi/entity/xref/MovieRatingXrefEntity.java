package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
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
@Table(name = MovieRatingXrefEntity.MOVIE_RATING_TABLE_NAME)
public class MovieRatingXrefEntity extends AbstractEntity {

    static final String MOVIE_RATING_TABLE_NAME = "movie_rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = MovieEntity.class)
    @JoinColumn(name = "MOVIE_ID")
    private MovieEntity movie;

    @ManyToOne(targetEntity = UsersEntity.class)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    private int rating;
}
