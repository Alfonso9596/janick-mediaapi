package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.SeriesEntity;
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
