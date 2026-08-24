package com.janickmediadb.janickmediaapi.input;

import com.janickmediadb.janickmediaapi.model.AbstractModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
