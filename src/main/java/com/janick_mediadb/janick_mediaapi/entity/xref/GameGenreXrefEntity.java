package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameGenreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = GameGenreXrefEntity.GAME_GENRE_TABLE_NAME)
public class GameGenreXrefEntity extends AbstractEntity {

    static final String GAME_GENRE_TABLE_NAME = "game_genre_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = GameEntity.class)
    @JoinColumn(name = "GAME_ID")
    private GameEntity game;

    @ManyToOne(targetEntity = GameGenreEntity.class)
    @JoinColumn(name = "GENRE_ID")
    private GameGenreEntity genre;
}
