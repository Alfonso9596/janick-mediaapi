package com.janick_mediadb.janick_mediaapi.input;

import com.janick_mediadb.janick_mediaapi.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeriesInput extends AbstractModel {

    private String name;
    private String description;
    private String yearStart;
    private String yearEnd;
    private Integer episodeLength;
    private List<String> genres;
}
