package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MovieGenreXrefEntity;
import com.janickmediadb.janickmediaapi.input.MovieInput;
import com.janickmediadb.janickmediaapi.model.MovieModel;
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

    public MovieModel toModel(String timezone) {
        MovieModel model = new MovieModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setYear(year);
        model.setLength(length);
        model.setPosterFilepath(posterFilepath);
        model.setCreatedAt(createdAt.atZone(ZoneId.of(timezone)));
        model.setLastUpdated(lastUpdated.atZone(ZoneId.of(timezone)));
        model.setUser(user.toModel());
        return model;
    }
}
