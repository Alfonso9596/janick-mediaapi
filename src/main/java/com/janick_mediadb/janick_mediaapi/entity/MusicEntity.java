package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MusicGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.input.MusicInput;
import com.janick_mediadb.janick_mediaapi.model.MusicModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = MusicEntity.MUSIC_TABLE_NAME)
public class MusicEntity extends AbstractEntity {

    static final String MUSIC_TABLE_NAME = "music";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String artist;
    @Lob
    @Column(columnDefinition = "text")
    private String description;
    private String year;
    private String posterFilepath;
    private Instant createdAt;
    private Instant lastUpdated;

    @ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "music")
    private Set<MusicGenreXrefEntity> musicGenreXrefs = new HashSet<>();

    public void fromInput(MusicInput musicInput) {
        this.name = musicInput.getName();
        this.artist = musicInput.getArtist();
        this.description = musicInput.getDescription();
        this.year = musicInput.getYear();
    }

    public MusicModel toModel(String timezone) {
        MusicModel model = new MusicModel();
        model.setId(id);
        model.setName(name);
        model.setArtist(artist);
        model.setDescription(description);
        model.setYear(year);
        model.setPosterFilepath(posterFilepath);
        model.setCreatedAt(createdAt.atZone(ZoneId.of(timezone)));
        model.setLastUpdated(lastUpdated.atZone(ZoneId.of(timezone)));
        model.setUser(user.toModel());
        return model;
    }
}
