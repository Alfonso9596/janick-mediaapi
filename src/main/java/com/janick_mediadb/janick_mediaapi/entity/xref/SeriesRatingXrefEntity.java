package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = SeriesRatingXrefEntity.SERIES_RATING_TABLE_NAME)
public class SeriesRatingXrefEntity extends AbstractEntity {

    static final String SERIES_RATING_TABLE_NAME = "series_rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = SeriesEntity.class)
    @JoinColumn(name = "SERIES_ID")
    private SeriesEntity series;

    @ManyToOne(targetEntity = UsersEntity.class)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    private int rating;
}
