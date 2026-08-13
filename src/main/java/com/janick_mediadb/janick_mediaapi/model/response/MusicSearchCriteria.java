package com.janick_mediadb.janick_mediaapi.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class MusicSearchCriteria implements Serializable {

    private int id;
    private String name;
    private String artist;
    private int year;
    private String genre;
}
