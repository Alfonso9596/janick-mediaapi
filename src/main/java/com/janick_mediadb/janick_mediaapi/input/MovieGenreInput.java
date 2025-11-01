package com.janick_mediadb.janick_mediaapi.input;

import com.janick_mediadb.janick_mediaapi.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieGenreInput extends AbstractModel {

    private String name;
}
