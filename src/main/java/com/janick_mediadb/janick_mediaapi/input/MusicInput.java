package com.janick_mediadb.janick_mediaapi.input;

import com.janick_mediadb.janick_mediaapi.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MusicInput extends AbstractModel {

    private String name;
    private String artist;
    private String description;
    private String year;
    private List<String> genres;
}
