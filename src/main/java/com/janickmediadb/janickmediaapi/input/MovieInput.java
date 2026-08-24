package com.janickmediadb.janickmediaapi.input;

import com.janickmediadb.janickmediaapi.model.AbstractModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieInput extends AbstractModel {

    private String name;
    private String description;
    private String year;
    private Integer length;
    private List<String> genres;
}
