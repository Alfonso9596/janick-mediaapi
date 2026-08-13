package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.MusicEntity;
import com.janick_mediadb.janick_mediaapi.entity.MusicGenreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = MusicGenreXrefEntity.MUSIC_GENRE_TABLE_NAME)
public class MusicGenreXrefEntity extends AbstractEntity {

    static final String MUSIC_GENRE_TABLE_NAME = "music_genre_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = MusicEntity.class)
    @JoinColumn(name = "MUSIC_ID")
    private MusicEntity music;

    @ManyToOne(targetEntity = MusicGenreEntity.class)
    @JoinColumn(name = "GENRE_ID")
    private MusicGenreEntity genre;
}
