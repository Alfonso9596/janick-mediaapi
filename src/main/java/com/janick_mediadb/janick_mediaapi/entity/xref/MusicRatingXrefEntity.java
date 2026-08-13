package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.MusicEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = MusicRatingXrefEntity.MUSIC_RATING_TABLE_NAME)
public class MusicRatingXrefEntity extends AbstractEntity {

    static final String  MUSIC_RATING_TABLE_NAME = "music_rating";

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
