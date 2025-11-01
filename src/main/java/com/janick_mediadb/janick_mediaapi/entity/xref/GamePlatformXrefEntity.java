package com.janick_mediadb.janick_mediaapi.entity.xref;

import com.janick_mediadb.janick_mediaapi.entity.AbstractEntity;
import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = GamePlatformXrefEntity.GAME_PLATFORM_TABLE_NAME)
public class GamePlatformXrefEntity extends AbstractEntity {

    static final String GAME_PLATFORM_TABLE_NAME = "game_platform_xref";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = GameEntity.class)
    @JoinColumn(name = "GAME_ID")
    private GameEntity game;

    @ManyToOne(targetEntity = GamePlatformEntity.class)
    @JoinColumn(name = "PLATFORM_ID")
    private GamePlatformEntity platform;
}
