package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.SeriesEntity;
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
@Table(name = SeriesGenreXrefEntity.SERIES_GENRE_TABLE_NAME)
public class SeriesGenreXrefEntity extends AbstractEntity {

    static final String SERIES_GENRE_TABLE_NAME = "series_genre_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = SeriesEntity.class)
    @JoinColumn(name = "SERIES_ID")
    private SeriesEntity series;

    @ManyToOne(targetEntity = MovieGenreEntity.class)
    @JoinColumn(name = "GENRE_ID")
    private MovieGenreEntity genre;
}
