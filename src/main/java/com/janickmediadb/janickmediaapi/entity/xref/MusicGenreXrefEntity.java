package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import com.janickmediadb.janickmediaapi.entity.MusicGenreEntity;
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
