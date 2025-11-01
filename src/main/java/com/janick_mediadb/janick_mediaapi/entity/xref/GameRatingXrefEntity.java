package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = GameRatingXrefEntity.GAME_RATING_TABLE_NAME)
public class GameRatingXrefEntity extends AbstractEntity {

    static final String GAME_RATING_TABLE_NAME = "game_rating";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = GameEntity.class)
    @JoinColumn(name = "GAME_ID")
    private GameEntity game;

    private int rating;
}
