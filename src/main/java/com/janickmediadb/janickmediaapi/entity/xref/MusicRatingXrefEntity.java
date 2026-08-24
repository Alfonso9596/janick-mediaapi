package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.MusicEntity;
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
@Table(name = MusicRatingXrefEntity.MUSIC_RATING_TABLE_NAME)
public class MusicRatingXrefEntity extends AbstractEntity {

    static final String MUSIC_RATING_TABLE_NAME = "music_rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = MusicEntity.class)
    @JoinColumn(name = "MUSIC_ID")
    private MusicEntity music;

    @ManyToOne(targetEntity = UsersEntity.class)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    private int rating;
}
