package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.input.MovieInput;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = MovieEntity.MOVIE_TABLE_NAME)
public class MovieEntity extends AbstractEntity {

    static final String MOVIE_TABLE_NAME = "movie";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob
    @Column(columnDefinition = "text")
    private String description;
    @Column(length = 4)
    private String year;
    private int length;
    private String posterFilepath;
    private Instant createdAt;
    private Instant lastUpdated;

    @ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;


    @OneToMany(mappedBy = "movie")
    private Set<MovieGenreXrefEntity> movieGenreXrefs = new HashSet<>();

    public void fromInput(MovieInput movieInput) {
        this.name = movieInput.getName();
        this.description = movieInput.getDescription();
        this.year = movieInput.getYear();
        this.length = movieInput.getLength();
    }

    public MovieModel toModel() {
        MovieModel model = new MovieModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setYear(year);
        model.setLength(length);
        model.setPosterFilepath(posterFilepath);
        model.setCreatedAt(Date.from(createdAt));
        model.setLastUpdated(Date.from(lastUpdated));
        model.setUser(user.toModel());
        return model;
    }
}
