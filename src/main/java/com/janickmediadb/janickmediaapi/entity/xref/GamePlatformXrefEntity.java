package com.janickmediadb.janickmediaapi.entity.xref;

import com.janickmediadb.janickmediaapi.entity.AbstractEntity;
import com.janickmediadb.janickmediaapi.entity.GameEntity;
import com.janickmediadb.janickmediaapi.entity.GamePlatformEntity;
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
