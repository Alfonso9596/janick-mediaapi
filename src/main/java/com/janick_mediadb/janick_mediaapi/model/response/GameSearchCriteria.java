package com.janick_mediadb.janick_mediaapi.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class GameSearchCriteria implements Serializable {

    private int id;
    private String name;
    private int year;
    private String genre;
    private String platform;
}
