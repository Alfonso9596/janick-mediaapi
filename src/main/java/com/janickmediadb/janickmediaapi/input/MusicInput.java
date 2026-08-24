package com.janickmediadb.janickmediaapi.input;

import com.janickmediadb.janickmediaapi.model.AbstractModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicInput extends AbstractModel {

    private String name;
    private String artist;
    private String description;
    private String year;
    private List<String> genres;
}
