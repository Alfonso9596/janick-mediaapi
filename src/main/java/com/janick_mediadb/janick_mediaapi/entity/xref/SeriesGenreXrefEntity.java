package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import jakarta.persistence.*;
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
